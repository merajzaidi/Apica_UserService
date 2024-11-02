package com.apica.UserService.service;

import com.apica.UserService.constant.Role;
import com.apica.UserService.dto.UserDetailsDTO;
import com.apica.UserService.dto.request.UserLoginDTO;
import com.apica.UserService.dto.request.UserRequestDTO;
import com.apica.UserService.dto.response.UserDTO;
import com.apica.UserEventData;
import com.apica.UserService.entity.Auth;
import com.apica.UserService.repository.AuthRepository;
import com.apica.UserService.security.JWTUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apica.UserService.constant.Topic.USER_EVENTS;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    AuthRepository authRepository;
    AuthenticationManager authenticationManager;
    JWTUtil jwtTokenUtil;

    KafkaTemplate<String, UserEventData> kafkaTemplate;

    @Override
    public List<UserDTO> getAllUsers() {
        return authRepository.findAll().stream().map(
                user -> UserDTO.builder()
                        .emailAddress(user.getEmail())
                        .phoneNumber(user.getPhoneNo())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public void register(UserRequestDTO userRequestDTO) {
        authRepository.save(Auth.builder().phoneNo(userRequestDTO.getPhoneNo()).role(userRequestDTO.getRole()).email(userRequestDTO.getEmailAddress()).password(userRequestDTO.getPassword()).build());
        sendEventToKafka(userRequestDTO.getEmailAddress(),"User Registered");
    }

    private void sendEventToKafka(String user, String event) {
        try {
            System.out.println("Data Sent");
            kafkaTemplate.send(USER_EVENTS.getTopic(), user, UserEventData.builder().event(event).user(user).build());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        Auth auth = authRepository.findByEmail(userLoginDTO.getEmailAddress()).orElseThrow();
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));
        UserDetailsDTO userDetails = (UserDetailsDTO) authenticate.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);
        sendEventToKafka(userLoginDTO.getEmailAddress(),"User Logged In");
        return token;
    }

    @Override
    public void update(UserRequestDTO userUpdateDTO) {
        Auth user = authRepository.findByEmail(userUpdateDTO.getEmailAddress()).orElseThrow();
        user.setEmail(userUpdateDTO.getEmailAddress());
        user.setPhoneNo(userUpdateDTO.getPhoneNo());
        sendEventToKafka(userUpdateDTO.getEmailAddress(),"User Details Updated");
        authRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        authRepository.deleteByEmail(email);
        sendEventToKafka(email,"User Deleted");
    }

    @Override
    public void updatePassword(UserLoginDTO userLoginDTO) {
        Auth user = authRepository.findByEmail(userLoginDTO.getEmailAddress()).orElseThrow();
        user.setPassword(user.getPassword());
        authRepository.save(user);
        sendEventToKafka(userLoginDTO.getEmailAddress(),"User updated the password");
    }

    @Override
    public UserDTO getUserDetail(String email) {
        Auth user = authRepository.findByEmail(email).orElseThrow();
        return UserDTO.builder().phoneNumber(user.getPhoneNo()).emailAddress(user.getEmail()).build();
    }

    @Override
    public boolean isAdmin() {
        return ((UserDetailsDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole() == Role.ADMIN;
    }
}
