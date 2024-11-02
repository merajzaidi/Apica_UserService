package com.apica.UserService.controller;

import com.apica.UserService.dto.request.UserLoginDTO;
import com.apica.UserService.dto.request.UserRequestDTO;
import com.apica.UserService.dto.response.ResponseDTO;
import com.apica.UserService.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    UserService userService;

    @GetMapping("/getAllUsers")
    @PreAuthorize("ADMIN")
    public ResponseEntity<ResponseDTO> getAllUsersDetails(){
        try {
            return ResponseEntity.ok(ResponseDTO.builder().success(true).data(userService.getAllUsers()).build());
        } catch(Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<ResponseDTO> isAdmin(){
        try {
            return ResponseEntity.ok(ResponseDTO.builder().success(true).data(userService.isAdmin()).build());
        } catch(Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }


    @GetMapping("/userDetail")
    public ResponseEntity<ResponseDTO> getUsersDetail(@RequestParam String email){
        try {
            return ResponseEntity.ok(ResponseDTO.builder().success(true).data(userService.getUserDetail(email)).build());
        } catch(Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserRequestDTO userRequestDTO){
        try{
            userService.register(userRequestDTO);
            return ResponseEntity.ok(ResponseDTO.builder().success(true).build());
        } catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO){
        try{
            return ResponseEntity.ok(ResponseDTO.builder().success(true).data(userService.login(userLoginDTO)).build());
        } catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }

    @PutMapping("/updateDetail")
    public ResponseEntity<ResponseDTO> update(@RequestBody UserRequestDTO userUpdateDTO){
        try{
            userService.update(userUpdateDTO);
            return ResponseEntity.ok(ResponseDTO.builder().success(true).build());
        } catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<ResponseDTO> update(@RequestBody UserLoginDTO userLoginDTO){
        try{
            userService.updatePassword(userLoginDTO);
            return ResponseEntity.ok(ResponseDTO.builder().success(true).build());
        } catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ResponseDTO> update(@RequestParam String email){
        try{
            userService.deleteUser(email);
            return ResponseEntity.ok(ResponseDTO.builder().success(true).build());
        } catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(ResponseDTO.builder().success(false).build());
        }
    }
}
