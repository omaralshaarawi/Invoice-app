package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.repository.RoleRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.ProviderService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Roles;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") // http://localhost:8888/invoice-app-ws/users
public class userController {

	@Autowired
	UserService userService;

	@Autowired
	ProviderService providerService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	// GETs User Details using id
	@Secured("ADMIN")
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserById(id);
		if (userDto == null)
			throw new UserServiceException("USER " + id + "NOT FOUND");
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);
		return returnValue;
	}

	// Creates a new user
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_ACCOUNTANT.name())));

		UserDto createdUser = userService.createUser(userDto);

		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;

	}

	// UPDATES USER
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id,
			@RequestParam(required = false) String Role) {
		UserRest returnValue = new UserRest();
		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		UserDto userDto = new UserDto();
		ModelMapper modelMapper = new ModelMapper();

		userDto = modelMapper.map(userDetails, UserDto.class);

		if (Role == "ACCOUNTANT")
			userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_ACCOUNTANT.name())));
		else if (Role == "CHEF_ACCOUNTANT")
			userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_CHEF_ACCOUNTANT.name())));
		else if (Role == "ADMIN")
			userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_ADMIN.name())));

		UserDto updateUser = userService.updateUser(id, userDto);
		returnValue = modelMapper.map(updateUser, UserRest.class);
		return returnValue;
	}

	// DELETS USER
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id, @RequestParam(required = false) boolean confirm) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperatioName("DELETE");
		if (confirm == false) {
			returnValue.setOperationResult("PLEASE CONFRIM DELEATION");
			return returnValue;
		}
		userService.deleteUser(id);
		returnValue.setOperationResult("SUCCESS");
		return returnValue;
	}

	// GETs All Users
	@Secured("ADMIN")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}

}
