package com.prophius.socialmediaservice.repository;

import com.prophius.socialmediaservice.dals.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findUserById(long id);

    @Query(value = "SELECT * FROM users WHERE phone_no=?1", nativeQuery = true)
    Optional<User> findByPhoneNo(String phoneNo);

    @Query(value = "SELECT id FROM users WHERE phone_no=?1", nativeQuery = true)
    Optional<Long> findUserIdByPhoneNo(String phoneNo);

    @Query(value = "SELECT username FROM users WHERE phone_no=?1", nativeQuery = true)
    Optional<String> findUsernameByPhoneNo(String phoneNo);
}
