package com.prophius.socialmediaservice.controller;

import com.prophius.socialmediaservice.dals.IUserDetails;
import com.prophius.socialmediaservice.dto.FUserDto;
import com.prophius.socialmediaservice.dto.ResponseDto;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import com.prophius.socialmediaservice.service.FollowershipService;
import com.prophius.socialmediaservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.prophius.socialmediaservice.enums.Authorities.USER_PREAUTHORIZE;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    private final FollowershipService followershipService;

    @GetMapping
    @CrossOrigin
    @PreAuthorize(USER_PREAUTHORIZE)
    public ResponseDto<?> getUser(Authentication authentication) throws CommonsException {
        long userId = IUserDetails.getId(authentication);
        FUserDto user = userService.findByUserId(userId);
        return ResponseDto.wrapSuccessResult(user, "request successful");
    }

    @PostMapping("/{userId}/follow")
    @PreAuthorize(USER_PREAUTHORIZE)
    public ResponseDto<?> handleUserFollowing(Authentication authentication, @Valid @PathParam("userId") Long followedId) {
        long followerId = IUserDetails.getId(authentication);
        followershipService.followUser(followerId, followedId);
        return ResponseDto.wrapSuccessResult(null, "request successful");
    }

    @PostMapping("/{userId}/un-follow")
    @PreAuthorize(USER_PREAUTHORIZE)
    public ResponseDto<?> handleUnFollowingUser(Authentication authentication, @Valid @PathParam("userId") Long followedId) {
        long followerId = IUserDetails.getId(authentication);
        followershipService.unfollowUser(followerId, followedId);
        return ResponseDto.wrapSuccessResult(null, "request successful");
    }

    @PostMapping("/me/followers")
    @PreAuthorize(USER_PREAUTHORIZE)
    public ResponseDto<?> handleFetchingFollowers(Authentication authentication, @RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                                  @RequestParam(required = false, name = "size", defaultValue = "20") int size) {
        long userId = IUserDetails.getId(authentication);
        return ResponseDto.wrapSuccessResult(followershipService.fetchUserFollowership(userId, page, size), "request successful");
    }
}
