package com.apica.UserService.security;

import com.apica.UserService.constant.Role;
import com.apica.UserService.dto.UserDetailsDTO;
import com.apica.UserService.entity.Auth;
import com.apica.UserService.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AuthRepository authRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Auth user = authRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		List<SimpleGrantedAuthority> roles;
		if (Objects.equals(user.getRole(), Role.ADMIN)) {
			roles = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (Objects.equals(user.getRole(), Role.USER)) {
			roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		} else {
			throw new UsernameNotFoundException("User not found with phoneNo: " + email);
		}
		return createUserDetails(user, roles);
	}

	public UserDetailsDTO createUserDetails(Auth user, List<SimpleGrantedAuthority> roles) {

		return new UserDetailsDTO(user.getEmail(),user.getPhoneNo(), (new BCryptPasswordEncoder()).encode(user.getPassword()), roles,
				user.getId(), user.getRole());
	}
}
