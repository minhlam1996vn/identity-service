package com.lamldm.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.lamldm.identity_service.dto.request.UserCreationRequest;
import com.lamldm.identity_service.entity.User;
import com.lamldm.identity_service.exception.AppException;
import com.lamldm.identity_service.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private User user;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        user = User.builder()
                .id("6b92af46")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertEquals("6b92af46", response.getId());
        Assertions.assertEquals("john", response.getUsername());
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        Assertions.assertEquals(1002, exception.getErrorCode().getCode());
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_valid_success() {
        var mockUser = new User();
        mockUser.setUsername("john");
        mockUser.setId("6b92af46");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));

        var response = userService.getMyInfo();

        Assertions.assertEquals("6b92af46", response.getId());
        Assertions.assertEquals("john", response.getUsername());
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_userNotFound_error() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertEquals(1005, exception.getErrorCode().getCode());
    }
}
