package com.apica.UserService.dto.request;

import com.apica.UserService.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRequestDTO{
    String phoneNo;
    String emailAddress;
    Role role;
    String password;
}
