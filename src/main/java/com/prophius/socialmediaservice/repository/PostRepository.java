package com.prophius.socialmediaservice.repository;

import com.prophius.socialmediaservice.dals.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
