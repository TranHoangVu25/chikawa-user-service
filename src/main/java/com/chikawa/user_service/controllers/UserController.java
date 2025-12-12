package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.request.UserUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.dto.response.UserResponse;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("api/v1/users")
public class UserController {
    UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> registerAccount(
            @RequestBody @Valid UserCreationRequest request
    ) {
        try {

            return userService.registerAccount(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            ApiResponse.<String>builder()
                                    .message(e.getMessage())
                                    .build()
                    );
        }
    }

    //xem tất cả các user để test
    @GetMapping()
    public ResponseEntity<ApiResponse<List<User>>> getAllUser(){
        return userService.getAllUser();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @RequestBody @Valid UserUpdateRequest request,
            @PathVariable Long userId
    ){
        return userService.updateUser(request,userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @PathVariable Long userId
    ){
        return userService.deleteUser(userId);
    }

    @PostMapping("/lock/{userId}")
    public ResponseEntity<ApiResponse<String>> lockUser(
            @PathVariable Long userId
    ) {
        return userService.lockUserAdminRole(userId);
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<User>> createAccount(
            @RequestBody @Valid UserCreationRequest request
    ) {
        return userService.createUserAdminRole(request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable Long userId
    ){
        return userService.getUserById(userId);
    }
}
