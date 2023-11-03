package com.prophius.socialmediaservice.dto;

import com.prophius.socialmediaservice.dals.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;
    private String username;

    public static UserDto toDto(User userModel) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(userModel, dto);
        return dto;
    }
}