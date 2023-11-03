package com.prophius.socialmediaservice.util;

import com.google.common.collect.Maps;
import com.prophius.socialmediaservice.dals.IAuthorities;
import com.prophius.socialmediaservice.dals.IUserDetails;
import com.prophius.socialmediaservice.dto.TokensDto;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import com.prophius.socialmediaservice.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.PrematureJwtException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.prophius.socialmediaservice.util.Constants.ENABLED;
import static com.prophius.socialmediaservice.util.Constants.EntityColumns.ID;
import static com.prophius.socialmediaservice.util.Constants.SCOPES;
import static com.prophius.socialmediaservice.util.Constants.TYPE;
import static com.prophius.socialmediaservice.util.JwtTokenUtils.TokenType.REFRESH;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final IJwtService iJwtService;

    private final JwtSecretUtils jwtSecret;


    private JwtBuilder buildJwt(IUserDetails userDetails, Map<String, Object> headers) throws JwtException, CommonsException {
        if (userDetails == null) throw new CommonsException("user not found", HttpStatus.NOT_FOUND);
        if (!userDetails.isEnabled()) throw new CommonsException("user not enabled", HttpStatus.FORBIDDEN);



        if (userDetails.getAuthorities().isEmpty())
            throw new CommonsException("user.has_no_privileges", HttpStatus.FORBIDDEN);

        String id = UUID.randomUUID().toString();
        long userId = userDetails.getId();
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        claims.setId(id);
        claims.put(SCOPES, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));
        claims.put(ID, userId);
        claims.put(ENABLED, userDetails.isEnabled());

        // save the token id async
        CompletableFuture.runAsync(() -> iJwtService.save(userId, id));

        return Jwts.builder()
                .setClaims(claims)
                .setHeader(headers)
                .setIssuer(jwtSecret.getIssuer())
                .setIssuedAt(new Date())
                .signWith(HS512, jwtSecret.getValue());
    }

    public TokensDto generateTokens(IUserDetails userDetails) throws JwtException, CommonsException {
        JwtBuilder jwtBuilder = buildJwt(userDetails, Maps.newTreeMap());
        LocalDateTime accessTokenLocalDateTime = LocalDateTime.now().plus(jwtSecret.getExpiration(), ChronoUnit.MINUTES);
        Date accessTokenExp = Date.from(accessTokenLocalDateTime.toInstant(ZoneOffset.UTC));
        String accessToken = jwtBuilder.claim(TYPE, TokenType.ACCESS.name).setExpiration(accessTokenExp).compact();

        Date notBfTokenExp = Date.from(LocalDateTime.now()
                .plus(jwtSecret.getRefreshTokenNBF(), ChronoUnit.MINUTES)
                .toInstant(ZoneOffset.UTC));
        Date refreshTokenExp = Date.from(LocalDateTime.now()
                .plus(jwtSecret.getRefreshTokenExpirationInDays(), ChronoUnit.DAYS)
                .toInstant(ZoneOffset.UTC));
        String refreshToken = jwtBuilder.claim(TYPE, REFRESH.name).setNotBefore(notBfTokenExp)
                .setExpiration(refreshTokenExp).compact();

        return TokensDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public String generateSoloAccessToken(IUserDetails iUserDetails, int expirationInMin, String... scopes) throws JwtException, CommonsException {
        JwtBuilder jwtBuilder = buildJwt(iUserDetails, Maps.newTreeMap());

        // remove scope and permissions from token so this token can only be used in API_USER level authority
        jwtBuilder.claim(SCOPES, scopes);

        LocalDateTime accessTokenLocalDateTime = LocalDateTime.now().plus(expirationInMin, ChronoUnit.MINUTES);
        Date accessTokenExp = Date.from(accessTokenLocalDateTime.toInstant(ZoneOffset.UTC));
        return jwtBuilder.claim(TYPE, TokenType.ACCESS.name).setExpiration(accessTokenExp).compact();
    }

    public JwtDto getValidJwtDetails(String token, TokenType tokenType) throws CommonsException {
        String message = "invalid.jwt.token";
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSecret.getValue())
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();
            String id = claims.getId();
            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            Long userId = claims.get(ID, Long.class);
            Boolean enabled = claims.get(ENABLED, Boolean.class);
            ArrayList<?> scopes = claims.get(SCOPES, ArrayList.class);
            String type = (String) claims.getOrDefault(TYPE, null);
            JwsHeader<?> header = jws.getHeader();

            if (tokenType.name.equals(type)) {
                return JwtDto.builder().valid(true).id(id).type(type).userId(userId)
                        .enabled(enabled).username(subject).header(header).scopes(scopes)
                        .expiration(expiration).build();
            }

            message = "invalid jwt token type";
        } catch (PrematureJwtException e) {
            message = "premature jwt exception";
        } catch (ExpiredJwtException e) {
            message = "expired jwt exception";
        } catch (Exception e) {
            log.debug(message, e.getMessage());
        }
        throw new CommonsException(message, HttpStatus.UNAUTHORIZED);
    }


    @Setter
    @Builder
    public static class JwtDto {
        public String id;
        public Long userId;
        public JwsHeader<?> header;
        public boolean valid;
        public ArrayList<?> scopes;
        public ArrayList<?> perms;
        public String username;
        public Date expiration;
        public String type;
        public boolean enabled;

        public IUserDetails asUser() {
            IUserDetails iUserDetails = new IUserDetails();
            iUserDetails.setId(userId == null ? 0 : userId);
            iUserDetails.setUsername(username);
            iUserDetails.setEnabled(enabled);

            List<IAuthorities> authorities = scopes.stream()
                    .map(scope -> IAuthorities.instance(username, (String) scope))
                    .collect(Collectors.toList());
            iUserDetails.setAuthorities(authorities);
            return iUserDetails;
        }
    }


    @Getter
    public enum TokenType {
        ACCESS("access"),
        REFRESH("refresh");

        private final String name;

        TokenType(String name) {
            this.name = name;
        }
    }
}
