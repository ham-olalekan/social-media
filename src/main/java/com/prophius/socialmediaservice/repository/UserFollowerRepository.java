package com.prophius.socialmediaservice.repository;

import com.prophius.socialmediaservice.dals.UserFollower;
import com.prophius.socialmediaservice.projection.UserFollowerProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowerRepository extends CrudRepository<UserFollower, Long> {

    @Query(value = "COUNT * FROM user_followers WHERE user_id=?1 AND user_followers=?2", nativeQuery = true)
    int findFollowership(long userId, long followerId);

    @Query(value = "DELETE * FROM user_followers WHERE user_id=?1 AND user_followers=?2")
    void deleteFollowership();

    @Query(value = "SELECT uf.follower_id AS followerId, u.first_name AS firstName, u.last_name AS lastName, u.username AS username" +
            " INNER JOIN users u ON u.id=uf.user_id WHERE uf.user_id=:userId LIMIT :page, :size", nativeQuery = true)
    List<UserFollowerProjection> fetchFollowership(@Param("userId") long userId,@Param("page") int page, @Param("limit") int size);
}
