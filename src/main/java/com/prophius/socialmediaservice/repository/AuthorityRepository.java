package com.prophius.socialmediaservice.repository;

import com.prophius.socialmediaservice.dals.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
