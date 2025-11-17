package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.request.AuthenticationRequest;
import com.chikawa.user_service.dto.request.IntrospectRequest;
import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.dto.response.AuthenticationResponse;
import com.chikawa.user_service.dto.response.IntrospectResponse;
import com.chikawa.user_service.services.AuthenticationService;
import com.chikawa.user_service.services.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws Exception {
        return authenticationService.authenticate(request);
        }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate (@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody UserCreationRequest request
    ) {
        return userService.createUser(request);
    }

    @GetMapping("/confirm")
    public ResponseEntity<ApiResponse<String>> confirmAccount(
            @RequestParam("token") String token
    ) {
        return userService.confirmUser(token);
    }

}
