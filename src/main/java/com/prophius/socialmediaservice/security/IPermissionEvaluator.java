package com.prophius.socialmediaservice.security;

import com.prophius.socialmediaservice.dals.IAuthorities;
import com.prophius.socialmediaservice.dals.IUserDetails;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName();
        return hasPrivilege(auth, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, permission.toString());
    }

    @SneakyThrows
    public static IUserDetails validatePermission(Authentication authentication, MessageSource messageSource,
                                                  Function<List<String>, Boolean> function,
                                                  String code, Object[] args, Locale locale) {
        IUserDetails iUserDetails = (IUserDetails) authentication.getPrincipal();
        if (function.apply(iUserDetails.getPermissions()))
            return iUserDetails;

        String errorMessage = messageSource.getMessage(code, args, locale);
        throw new CommonsException(errorMessage, HttpStatus.FORBIDDEN);
    }

    @SneakyThrows
    private boolean hasPrivilege(Authentication auth, String permission) {
        IUserDetails iUserDetails = (IUserDetails) auth.getPrincipal();
        Collection<IAuthorities> authorities = iUserDetails.getAuthorities();
        if (authorities == null || authorities.isEmpty()) return false;

        if (iUserDetails.getAuthorities().stream()
                .noneMatch(userPermission -> authorities.stream().anyMatch(userPermission::equals))) {
            throw new CommonsException("Unauthorized to access this resource", HttpStatus.FORBIDDEN);
        }

        return true;
    }

    public boolean hasPermission(String targetId, String category, String code){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return hasPermission(authentication, targetId, category, code);
        } catch (Exception ignored){}
        return false;
    }
}
