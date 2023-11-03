package com.prophius.socialmediaservice.dals;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity(name = "users_follower")
public class UserFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false)
    private long userId;

    @Column(nullable = false, updatable = false)
    private long followerUserId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;
}
