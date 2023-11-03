package com.prophius.socialmediaservice.dto;

import com.prophius.socialmediaservice.annotations.DoesNotExist;
import com.prophius.socialmediaservice.annotations.ValidCountryCode;
import com.prophius.socialmediaservice.annotations.ValidEmailAddress;
import com.prophius.socialmediaservice.annotations.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import static com.prophius.socialmediaservice.util.Constants.EntityColumns.EMAIL;
import static com.prophius.socialmediaservice.util.Constants.EntityColumns.PHONE_NO;
import static com.prophius.socialmediaservice.util.Constants.EntityColumns.USERS;

@Getter
@Setter
@Valid
public class CreateUserDto implements ICreateUserDto {
    @Size(max = 20, min = 2, message = "firstname.accepted_length")
    @NotBlank(message = "firstname.not_blank")
    private String firstName;

    @Size(max = 20, min = 2, message = "lastname.accepted_length")
    @NotBlank(message = "lastname.not_blank")
    private String lastName;

    @ValidPhoneNumber
    @NotBlank(message = "phone_number.not_blank")
    @DoesNotExist(table = USERS, columnName = PHONE_NO, message = "user phone_no exists")
    private String phoneNo;

    @ValidEmailAddress
    @NotBlank(message = "email not blank")
    @DoesNotExist(table = USERS, columnName = EMAIL, message = "user email exists")
    private String email;

    @ValidCountryCode
    @NotBlank(message = "country_code.not_blank")
    private String countryCode;

    private String referralCode;

    private String source = "MOBILE_APP";
    private String password;

    @Override
    public String getPassword() {
        return password;
    }

    public String getSource() {
        if (StringUtils.hasText(source)) return source;
        return "MOBILE_APP";
    }
}
