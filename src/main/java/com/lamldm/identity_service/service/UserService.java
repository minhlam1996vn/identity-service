package com.lamldm.identity_service.service;

import com.lamldm.identity_service.dto.request.UserCreationRequest;
import com.lamldm.identity_service.dto.request.UserUpdateRequest;
import com.lamldm.identity_service.dto.response.UserResponse;
import com.lamldm.identity_service.entity.User;
import com.lamldm.identity_service.exception.AppException;
import com.lamldm.identity_service.exception.ErrorCode;
import com.lamldm.identity_service.mapper.UserMapper;
import com.lamldm.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        return userRepository.save(user);
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }
}
