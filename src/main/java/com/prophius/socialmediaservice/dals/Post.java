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

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String reference;

    @Column(length = 300)
    private String content;

    @Column(nullable = false, updatable = false)
    private long userId;

    private BigDecimal likes;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @CreationTimestamp
    private Date updatedAt;

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