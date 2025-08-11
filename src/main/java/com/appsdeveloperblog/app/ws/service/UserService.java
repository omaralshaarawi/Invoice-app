package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.UserDto;

@Service
public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);

	UserDto getUser(String email);

	UserDto getUserById(String id);

	UserDto updateUser(String userId, UserDto User);

	void deleteUser(String userId);

	List<UserDto> getUsers(int page, int limit);
}
