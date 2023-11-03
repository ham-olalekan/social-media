package com.prophius.socialmediaservice.service;


import com.prophius.socialmediaservice.dals.IAuthorities;
import com.prophius.socialmediaservice.dals.IUserDetails;
import com.prophius.socialmediaservice.enums.Authorities;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import com.prophius.socialmediaservice.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.prophius.socialmediaservice.util.Constants.BASIC_AUTHS;
import static com.prophius.socialmediaservice.util.Constants.BASIC_AUTH_PREFIX;


@Service
@Transactional
@RequiredArgsConstructor
public class IUserService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(BASIC_AUTH_PREFIX))
            return findBasicAuthByUsername(username);
        return findFullUserDetailsByUsername(username);
    }

    public IUserDetails findFullUserDetailsByUsername(String username) throws NotFoundException {
        IUserDetails iUserDetails = findUserDetailsByUsername(username);
        iUserDetails.setAuthorities(findUserAuthoritiesByUsername(username));
        return iUserDetails;
    }

    @Cacheable("findUserMinorByUsername")
    public IUserDetails findUserDetailsByUsername(String username) throws NotFoundException {
        String query = "SELECT id, username, email, phone_no AS phoneNo, password, enabled FROM users WHERE username=?";
        return findUser(query, username);
    }

    private IUserDetails findUser(String query, String arg) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(IUserDetails.class), arg);
        } catch (EmptyResultDataAccessException ignored) {
        }
        throw new NotFoundException("user not found", HttpStatus.NOT_FOUND);
    }

    @Cacheable("findUserAuthoritiesByUsername")
    public List<IAuthorities> findUserAuthoritiesByUsername(String username) throws NotFoundException {
        String query = "SELECT authority FROM authorities WHERE username=?";
        return Optional.of(jdbcTemplate.queryForStream(query,
                        new BeanPropertyRowMapper<>(IAuthorities.class), username))
                .orElseThrow(() -> new NotFoundException("user not found", HttpStatus.NOT_FOUND))
                .collect(Collectors.toList());
    }

    private IUserDetails findBasicAuthByUsername(String username) throws CommonsException {
        String query = "SELECT username, password, enabled FROM " + BASIC_AUTHS + " WHERE username=?";
        IUserDetails user = Optional.ofNullable(jdbcTemplate.queryForObject(query,
                        new BeanPropertyRowMapper<>(IUserDetails.class), username))
                .orElseThrow(() -> new CommonsException("basic.auth.user.not.found", HttpStatus.NOT_FOUND));
        IAuthorities authorities = new IAuthorities();
        authorities.setUsername(username);
        authorities.setAuthority(Authorities.API_USER.name());
        user.setAuthorities(Collections.singletonList(authorities));
        return user;
    }
}
