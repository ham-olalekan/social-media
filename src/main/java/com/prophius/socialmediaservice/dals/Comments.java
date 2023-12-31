package com.prophius.socialmediaservice.dals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String reference;

    @Column(length = 300)
    private String content;

    @Column(nullable = false, updatable = false)
    private long userId;

    @Column(nullable = false, updatable = false)
    private long postId;

    @Column(updatable = false)
    private Long upstream_id;

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
