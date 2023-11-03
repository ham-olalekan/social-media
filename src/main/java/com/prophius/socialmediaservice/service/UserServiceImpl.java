package com.prophius.socialmediaservice.service;

import com.prophius.socialmediaservice.dals.Authority;
import com.prophius.socialmediaservice.dals.User;
import com.prophius.socialmediaservice.dto.CreateUserDto;
import com.prophius.socialmediaservice.dto.FUserDto;
import com.prophius.socialmediaservice.dto.UserDto;
import com.prophius.socialmediaservice.enums.Authorities;
import com.prophius.socialmediaservice.exceptions.CommonsException;
import com.prophius.socialmediaservice.repository.AuthorityRepository;
import com.prophius.socialmediaservice.repository.UserRepository;
import javax.transaction.Transactional;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import liquibase.repackaged.org.apache.commons.lang3.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Primary
@Transactional
@Slf4j
public class UserServiceImpl extends IUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(JdbcTemplate jdbcTemplate,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           AuthorityRepository authorityRepository) {
        super(jdbcTemplate);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;

    }

    @Transactional
    public UserDto registerNewUser(CreateUserDto signupDto) {
        UserDto userDto = createUser(signupDto);
        final String username = userDto.getUsername();
        createUserDefaultRoles(username, Authorities.DEFAULT());
        return userDto;
    }

    public UserDto createUser(CreateUserDto createUserDto) {
        User user = new User();
        String username = generateRandomUsername();
        BeanUtils.copyProperties(createUserDto, user);
        user.setUsername(username);
        final String hashedPassword = passwordEncoder.encode(createUserDto.getPassword());
        user.setPassword(hashedPassword);
        return UserDto.toDto(saveUser(user));
    }

    public void createUserDefaultRoles(String username, Authorities... authorities) {
        Set<Authority> authoritySet = new HashSet<>();
        for (Authorities authority : authorities) {
            Authority userAuthority = new Authority();
            userAuthority.setAuthority(authority.name());
            userAuthority.setUsername(username);
            authoritySet.add(userAuthority);
        }
        authorityRepository.saveAll(authoritySet);
    }

    /**
     * this function generates a random alphanumeric string for username
     * `nextInt` is used to condense the username string by picking a random number from 99-999
     * this further pushes collision
     *
     * @return String | alphanumeric + nextInt
     */
    private String generateRandomUsername() {
        String username = RandomStringUtils.randomAlphanumeric(10);
        String nextInt = String.valueOf(RandomUtils.nextInt(99, 999));
        return username + nextInt;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public FUserDto findByUserId(long userId) throws CommonsException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new CommonsException("user not found", HttpStatus.NOT_FOUND));
        return FUserDto.toDTO(user);
    }
}
