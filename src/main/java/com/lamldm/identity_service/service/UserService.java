package com.lamldm.identity_service.service;

import com.lamldm.identity_service.dto.request.UserCreationRequest;
import com.lamldm.identity_service.dto.request.UserUpdateRequest;
import com.lamldm.identity_service.dto.response.UserResponse;
import com.lamldm.identity_service.entity.User;
import com.lamldm.identity_service.enums.Role;
import com.lamldm.identity_service.exception.AppException;
import com.lamldm.identity_service.exception.ErrorCode;
import com.lamldm.identity_service.mapper.UserMapper;
import com.lamldm.identity_service.repository.RoleRepository;
import com.lamldm.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        log.info("Service: create user");
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }

//    @PreAuthorize("hasRole('ADMIN')") // hasRole -> sẽ tự động thêm prefix "ROLE_" phía trước
    @PreAuthorize("hasAnyAuthority('UPDATE_DATA')") // Dùng với permission
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    // Kiểm tra điều kiện với user đang đăng nhập sau khi vào method
    public UserResponse getUser(String id) {
        log.info("In method get User by Id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
}
