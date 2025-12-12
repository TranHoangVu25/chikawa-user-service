package com.chikawa.user_service.services;

import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.request.UserUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    ResponseEntity<ApiResponse<List<User>>> getAllUser ();

    ResponseEntity<ApiResponse<String>> registerAccount(UserCreationRequest request);

    ResponseEntity<ApiResponse<User>> updateUser(UserUpdateRequest request, Long id);

    ResponseEntity<ApiResponse<?>> deleteUser(Long id);

    ResponseEntity<ApiResponse<String>> confirmUser(String token);

    ResponseEntity<ApiResponse<User>> createUserAdminRole(UserCreationRequest request);

    ResponseEntity<ApiResponse<String>> lockUserAdminRole(Long userId);
}
