package com.apica.UserService.dto;

import com.apica.UserService.constant.Role;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsDTO extends User {

	public UserDetailsDTO(String emailId,String phoneNo, String password, Collection<? extends GrantedAuthority> authorities, UUID id,
						   Role role) {
		super(emailId, password, authorities);
		this.emailId = emailId;
		this.id = id;
		this.role = role;
		this.phoneNo = phoneNo;
		this.password = password;
	}

	UUID id;

	Role role;

	String phoneNo;
	String password;
	String emailId;
}
