package com.apica.UserService.service;

import com.apica.UserService.dto.request.UserLoginDTO;
import com.apica.UserService.dto.request.UserRequestDTO;
import com.apica.UserService.dto.response.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    void register(UserRequestDTO userRequestDTO);

    String login(UserLoginDTO userLoginDTO);

    void update(UserRequestDTO userUpdateDTO);

    void deleteUser(String email);

    void updatePassword(UserLoginDTO userLoginDTO);

    UserDTO getUserDetail(String email);

    boolean isAdmin();
}
