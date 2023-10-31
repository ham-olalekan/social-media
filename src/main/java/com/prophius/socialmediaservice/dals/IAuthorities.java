package com.prophius.socialmediaservice.dals;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@MappedSuperclass
public class IAuthorities implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    public static IAuthorities instance(String username, String authority) {
        IAuthorities authorities = new IAuthorities();
        authorities.setAuthority(authority);
        authorities.setUsername(username);
        return authorities;
    }
}
