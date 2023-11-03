package com.prophius.socialmediaservice.service;

import com.prophius.socialmediaservice.dals.UserFollower;
import com.prophius.socialmediaservice.projection.UserFollowerProjection;
import com.prophius.socialmediaservice.repository.UserFollowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FollowershipServiceImpl implements FollowershipService {

    private final UserFollowerRepository repository;

    @Override
    public void followUser(long follower, long followed) {
        int count = repository.findFollowership(followed, follower);
        if (count > 0) return;
        repository.save(UserFollower
                .builder()
                .followerUserId(follower)
                .userId(followed)
                .build());
    }

    @Override
    public void unfollowUser(long follower, long followed) {
        repository.deleteFollowership();
    }

    @Override
    public List<UserFollowerProjection> fetchUserFollowership(long userId, int page, int size) {
        return repository.fetchFollowership(userId, page, size);
    }
}
