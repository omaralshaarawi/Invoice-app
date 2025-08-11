package com.appsdeveloperblog.app.ws.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.RoleEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.RoleRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.shared.dto.UserDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    
    @Mock
    RoleRepository roleRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUserId("abc123");
        userEntity.setEncryptedPassword("encryptedPassword");
        userEntity.setEmail("test@example.com");
    }

    @Test
    void testGetUserByUserId_UserExists() {
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUserById("abc123");

        assertNotNull(userDto);
        assertEquals("John", userDto.getFirstName());
        assertEquals("abc123", userDto.getUserId());
    }

    @Test
    void testGetUserByUserId_UserNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        assertThrows(UserServiceException.class,
                () -> userService.getUserById("notfound"));
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn("generatedId");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(roleRepository.findByName(anyString())).thenAnswer(invocation -> {
            String roleName = invocation.getArgument(0);
            RoleEntity role = new RoleEntity();
            role.setName(roleName);
            return role;
        });


        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("123456");
        userDto.setEmail("tesst@example.com");
        userDto.setRoles(Arrays.asList("ROLE_ACCOUNTANT"));
        UserDto storedUser = userService.createUser(userDto);

        assertNotNull(storedUser);
        assertEquals("John", storedUser.getFirstName());
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_AlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("123456");
        userDto.setEmail("test@example.com");

        assertThrows(UserServiceException.class, () -> userService.createUser(userDto));
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

        assertDoesNotThrow(() -> userService.deleteUser("abc123"));
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        assertThrows(UserServiceException.class, () -> userService.deleteUser("notfound"));
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("abc123");
        userEntity.setFirstName("OldName");
        userEntity.setLastName("OldLast");

        // Arrange role
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("ROLE_ACCOUNTANT");

        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(roleRepository.findByName(anyString())).thenReturn(roleEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("UpdatedName");
        userDto.setLastName("UpdatedLast");
        userDto.setRoles(Arrays.asList("ROLE_ACCOUNTANT"));

        // Act
        UserDto updatedUser = userService.updateUser("abc123", userDto);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("UpdatedName", updatedUser.getFirstName());
        assertEquals("UpdatedLast", updatedUser.getLastName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }


    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        UserDto userDto = new UserDto();
        userDto.setFirstName("UpdatedName");

        assertThrows(UserServiceException.class, () -> userService.updateUser("notfound", userDto));
    }
}
