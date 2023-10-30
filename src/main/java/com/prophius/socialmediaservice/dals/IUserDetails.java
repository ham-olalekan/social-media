package com.prophius.socialmediaservice.dals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IUserDetails implements UserDetails, Serializable {
    private long id;
    private String email;
    private String username;
    private String phoneNo;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private String source;
    private Collection<IAuthorities> authorities = Collections.emptyList();
    private boolean enabled;
    private List<String> permissions = new ArrayList<>();

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    public static long getId(Authentication authentication) {
        IUserDetails iUserDetails = (IUserDetails) authentication.getPrincipal();
        return iUserDetails.getId();
    }

    public User toUser() {
        return new User(this.username, null, this.authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }
}
