package com.chikawa.user_service.services;

import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.request.UserUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.exception.ErrorCode;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<List<User>>> getAllUser() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.ok()
                    .body(
                            ApiResponse.<List<User>>builder()
                                    .message("No users found")
                                    .build()
                    );
        }
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<User>>builder()
                                .result(users)
                                .build()
                );
    }
    @Override
    public ResponseEntity<ApiResponse<User>> createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<User>builder()
                                    .code(ErrorCode.USER_EXISTED.getCode())
                                    .message(ErrorCode.USER_EXISTED.getMessage())
                                    .build()
                    );
        }
        String jti = UUID.randomUUID().toString();

        User user = new User().builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .jti(jti)
                .encryptedPassword(request.getEncryptedPassword()) //need update
                .confirmationToken(request.getConfirmationToken()) //need update
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(request.getRole())
                .signInCount(0)
                .dob(request.getDob())
                .build();

        return ResponseEntity.ok()
                .body(
                        ApiResponse.<User>builder()
                                .message("Create user successfully")
                                .result(userRepository.save(user))
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<User>> updateUser(UserUpdateRequest request, Long id) {
        if (!userRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<User>builder()
                                    .code(ErrorCode.USER_NOT_EXISTED.getCode())
                                    .message(ErrorCode.USER_NOT_EXISTED.getMessage())
                                    .build()
                    );
        }
        User user = userRepository.findById(id).get();
        user.setFullName(request.getFullName());
        user.setEncryptedPassword(request.getEncryptedPassword());
        user.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<User>builder()
                                .message("Update user successfully")
                                .result(userRepository.save(user))
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<User>builder()
                                    .code(ErrorCode.USER_NOT_EXISTED.getCode())
                                    .message(ErrorCode.USER_NOT_EXISTED.getMessage())
                                    .build()
                    );
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok()
                .body(
                        ApiResponse.builder()
                                .message("Delete user successfully")
                                .build()
                );
    }
}
