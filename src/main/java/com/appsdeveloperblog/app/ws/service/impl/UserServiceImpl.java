package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.RoleRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.security.UserPrincipal;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;


@Service
public class UserServiceImpl implements UserService {

	@Autowired																																																																																																																																																		
	UserRepository userRepository;
	
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public UserDto createUser(UserDto user) {
		
				
		if(userRepository.findByEmail(user.getEmail())!=null)
		{
			throw new RuntimeException("Record already exits");
		}
			
		
		UserEntity userEntity = new UserEntity();
		ModelMapper modelMapper = new ModelMapper();
		userEntity= modelMapper.map(user, UserEntity.class);
		
		String publicUserId=utils.generateUserId(30);		
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		//Set Roles
		Collection<RoleEntity> roleEntities = new HashSet<>();
		for(String role: user.getRoles())
		{
			RoleEntity roleEntity = roleRepository.findByName(role);
			if(roleEntity!=null)
			{
				roleEntities.add(roleEntity);
			}
		}
		
		
		userEntity.setRoles(roleEntities);
		UserEntity storedUserDetails= userRepository.save(userEntity);	
		
		UserDto returnValue= new UserDto();
		returnValue= modelMapper.map(storedUserDetails, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity= userRepository.findByEmail(username);
		if(userEntity==null)
		{
			throw new UsernameNotFoundException(username);
		}
		return new UserPrincipal(userEntity);
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity= userRepository.findByEmail(email);
		if(userEntity==null)
		{
			throw new UsernameNotFoundException(email);
		}
		UserDto returnValue= new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserById(String id) {
	UserDto returnValue= new UserDto();
	UserEntity userEntity=userRepository.findByUserId(id);
	ModelMapper modelMapper = new ModelMapper();

	if(userEntity==null)throw new UsernameNotFoundException(id);
	returnValue= modelMapper.map(userEntity, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserDto returnValue= new UserDto();
		UserEntity userEntity=userRepository.findByUserId(userId);
		if(userEntity==null)throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setEmail(user.getEmail());
		
		Collection<RoleEntity> roleEntities = new HashSet<>();
		for(String role: user.getRoles())
		{
			RoleEntity roleEntity = roleRepository.findByName(role);
			if(roleEntity!=null)
			{
				roleEntities.add(roleEntity);
			}
		}
		userEntity.setRoles(roleEntities);

		UserEntity updatedUserDetails=userRepository.save(userEntity);
		ModelMapper modelMapper = new ModelMapper();
		returnValue= modelMapper.map(updatedUserDetails, UserDto.class);

		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null)throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page,limit);
		Page<UserEntity> usersPage=userRepository.findAll(pageableRequest);
		List<UserEntity> users= usersPage.getContent();
		for(UserEntity userEntity : users)
		{
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		return returnValue;
	}
	
}
