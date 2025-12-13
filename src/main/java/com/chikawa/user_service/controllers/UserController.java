package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.request.ChangePasswordRequest;
import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.request.UserUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.dto.response.UserResponse;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.services.AuthenticationService;
import com.chikawa.user_service.services.UserService;
import com.chikawa.user_service.utils.UserContextHolder;
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
    AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> registerAccount(
            @RequestBody @Valid UserCreationRequest request
    ) {
            return userService.registerAccount(request);
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

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
    ){
        Long userId = UserContextHolder.getUserId();
        return userService.getUserById(userId);
    }

    @PostMapping("change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody @Valid ChangePasswordRequest request
    ){
        Long userId = UserContextHolder .getUserId();
        return authenticationService.changePassword(userId,request);
    }
}
