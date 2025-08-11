package com.appsdeveloperblog.app.ws.ui.controller;

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    userController userController;

    @Mock
    UserService userService;

    UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setUserId("abc123");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john@doe.com");
    }

    @Test
    void testGetUser() {
        when(userService.getUserById(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser("abc123");

        assertNotNull(userRest);
        assertEquals("John", userRest.getFirstName());
        assertEquals("abc123", userRest.getUserId());
        verify(userService, times(1)).getUserById("abc123");
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        UserDetailsRequestModel requestModel = new UserDetailsRequestModel();
        requestModel.setFirstName("John");
        requestModel.setLastName("Doe");
        requestModel.setEmail("john@doe.com");
        requestModel.setPassword("123");
        
        UserRest userRest = userController.createUser(requestModel);

        assertNotNull(userRest);
        assertEquals("John", userRest.getFirstName());
        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(anyString(), any(UserDto.class))).thenReturn(userDto);

        UserDetailsRequestModel requestModel = new UserDetailsRequestModel();
        requestModel.setFirstName("Updated");
        requestModel.setLastName("User");
        UserRest userRest = userController.updateUser(requestModel,"abc123","ADMIN");

        assertNotNull(userRest);
        assertEquals("John", userRest.getFirstName()); // Returned mock dto still says "John"
        verify(userService, times(1)).updateUser(eq("abc123"), any(UserDto.class));
    }

    @Test
    void testDeleteUser() {
        userController.deleteUser("abc123",true);
        verify(userService, times(1)).deleteUser("abc123");
    }

    @Test
    void testGetUsers() {
        when(userService.getUsers(anyInt(), anyInt())).thenReturn(Arrays.asList(userDto));

        var users = userController.getUsers(1, 1);
        assertEquals(1, users.size());
        verify(userService, times(1)).getUsers(1, 1);
    }
}
