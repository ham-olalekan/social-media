package com.prophius.socialmediaservice.dals;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String reference;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String countryCode;

    @Column(unique = true, nullable = false)
    private String phoneNo;

    @Column
    private String imageUrl;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private Date updatedAt;

    @Column(nullable = false)
    private boolean enabled = true;

    @PrePersist
    public void appendReference(){
        if (!StringUtils.hasText(this.reference)) {
            this.reference = UUID.randomUUID().toString().replaceAll("-", "");
        }
    }

    public String getReference(){
        return String.format("%s_%s", id, reference);
    }
}
