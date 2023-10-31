package com.prophius.socialmediaservice.repository;

import com.prophius.socialmediaservice.dals.Comments;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comments, Long> {

}
