package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.request.UserUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> createUser(
            @RequestBody @Valid UserCreationRequest request
    ) {
        return userService.createUser(request);
    }

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
}
