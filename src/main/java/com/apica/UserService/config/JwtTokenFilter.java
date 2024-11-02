package com.apica.UserService.config;

import com.apica.UserService.dto.UserDetailsDTO;
import com.apica.UserService.exception.ExceptionResponse;
import com.apica.UserService.security.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JWTUtil jwtTokenUtil;
	private final UserDetailsService userDao;

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Get authorization header and validate
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (isEmpty(header) || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		try {
			// Get jwt token and validate
			final String token = header.split(" ")[1].trim();
			if (!jwtTokenUtil.validateToken(token)) {
				chain.doFilter(request, response);
				return;
			}
			UserDetailsDTO userDetails = (UserDetailsDTO) userDao
					.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
			if (userDetails.isEnabled()) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, userDetails.getPassword(), jwtTokenUtil.getRolesFromToken(token));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (BadCredentialsException badCredentialsException) {
			response.getWriter().write(objectMapper.writeValueAsString(
					ExceptionResponse.builder().error(badCredentialsException.getMessage()).build()));
			response.setStatus(403);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			return;
		}
		chain.doFilter(request, response);
	}
}
