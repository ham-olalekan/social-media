package com.prophius.socialmediaservice.service;

import com.prophius.socialmediaservice.dto.UserFollowerDto;
import com.prophius.socialmediaservice.projection.UserFollowerProjection;

import java.util.List;

public interface FollowershipService {

    void followUser(long follower, long followed);

    void unfollowUser(long follower, long followed);

    List<UserFollowerProjection> fetchUserFollowership(long userId, int page, int size);
}
