package com.chikawa.user_service.services;

import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.request.UserUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.exception.ErrorCode;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.repositories.UserRepository;
import com.chikawa.user_service.utils.SendEmail;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    SendEmail sendEmail;

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
    public ResponseEntity<ApiResponse<String>> createUser(UserCreationRequest request) {
        //kiểm tra user đã tồn tại ch = email
        if (userRepository.existsByEmail(request.getEmail())){
            User user_existing = userRepository.findByEmail(request.getEmail())
                    .orElse(null);

            //nếu user đã tồn tại nhưng chưa được confirm thì resend email
            if (user_existing.getConfirmedAt() == null) {
                String confirm_token = UUID.randomUUID().toString();

                user_existing.setConfirmationToken(confirm_token);
                user_existing.setConfirmationSentAt(LocalDateTime.now());

                userRepository.save(user_existing);

                sendEmail.sendEmailRegister(confirm_token,request.getEmail());

                return ResponseEntity.ok()
                        .body(
                                ApiResponse.<String>builder()
                                        .message("Resend successfully!")
                                        .build()
                        );
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<String>builder()
                                    .code(ErrorCode.USER_EXISTED.getCode())
                                    .message(ErrorCode.USER_EXISTED.getMessage())
                                    .build()
                    );
        }
        String confirmToken = UUID.randomUUID().toString();

        String password = passwordEncoder.encode(request.getEncryptedPassword());

        User user = new User().builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .jti(request.getJti())
                .encryptedPassword(password)
                .confirmationToken(confirmToken)
                .confirmationSentAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(request.getRole())
                .signInCount(0)
                .dob(request.getDob())
                .build();

        userRepository.save(user);

        //method gửi email
        sendEmail.sendEmailRegister(confirmToken,user.getEmail());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Verification email sent. Please check your inbox.")
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
        if(request.getEncryptedPassword()==null){
            user.setEncryptedPassword(user.getEncryptedPassword());
        }else {
            String password = passwordEncoder.encode(request.getEncryptedPassword());
            user.setEncryptedPassword(password);
        }

        user.setFullName(request.getFullName());
//        user.setEncryptedPassword(request.getEncryptedPassword());
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

    //hàm xác nhận user khi chọn xác nhận trong mail
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> confirmUser(String token) {

        User user = userRepository.findByConfirmationToken(token)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .code(ErrorCode.INVALID_TOKEN.getCode())
                            .message("Invalid or expired token")
                            .build());
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setConfirmationToken(null);   // Xoá token sau khi xác nhận
        user.setUpdatedAt(LocalDateTime.now());
        user.setConfirmedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Account confirmed successfully")
                        .build()
        );
    }

}
