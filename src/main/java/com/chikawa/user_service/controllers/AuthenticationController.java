package com.chikawa.user_service.controllers;

import com.chikawa.user_service.configuration.CustomJwtDecoder;
import com.chikawa.user_service.dto.ForgotPasswordDTO;
import com.chikawa.user_service.dto.request.AuthenticationRequest;
import com.chikawa.user_service.dto.request.ChangePasswordRequest;
import com.chikawa.user_service.dto.request.IntrospectRequest;
import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.dto.response.AuthenticationResponse;
import com.chikawa.user_service.dto.response.IntrospectResponse;
import com.chikawa.user_service.exception.ErrorCode;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.repositories.UserRepository;
import com.chikawa.user_service.services.AuthenticationService;
import com.chikawa.user_service.services.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;
    CustomJwtDecoder customJwtDecoder;
    UserRepository userRepository;

    //truyền tài khoản mật khẩu vào sẽ trả về token (jwt)
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws Exception {
        return authenticationService.authenticate(request);
        }

        //truyền token vào sẽ trả về valid true or false
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

    //khách hàng ấn vào confirm trong email sẽ được chuyển đến api này
    //và thực hiện xác nhận
    @GetMapping("/confirm")
    public ResponseEntity<ApiResponse<String>> confirmAccount(
            @RequestParam("token") String token
    ) {
        String result = userService.confirmUser(token).getBody().getMessage();

        String redirectUrl;

        if ("Failed".equalsIgnoreCase(result)) {
            redirectUrl = "https://your-frontend.com/error?reason=confirm_failed";
        } else {
            redirectUrl = "http://localhost:5173/account/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302 Redirect
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request)
                    .getBody().getResult();

            String jwt = response.getToken();
            Jwt decodedJwt = customJwtDecoder.decode(jwt);
            String scope = decodedJwt.getClaimAsString("scope");
            Long userId = Long.valueOf(decodedJwt.getClaimAsString("userId"));

            User u = userRepository.findById(userId).orElseThrow();
            User user = new User().builder()
                    .email(u.getEmail())
                    .id(u.getId())
                    .fullName(u.getFullName())
                    .lineUserId(u.getLineUserId())
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(
                            ApiResponse.<User>builder()
                                    .message("Login with role: " + scope)
                                    .result(user)
                                    .build()
                    );

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(
                            ApiResponse.<User>builder()
                                    .code(ErrorCode.ACCOUNT_PASSWORD_NOT_CORRECT.getCode())
                                    .message(ErrorCode.ACCOUNT_PASSWORD_NOT_CORRECT.getMessage())
                                    .build()
                    );
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordDTO forgotPasswordDTO
            ){
            return authenticationService.forgotPassWord(forgotPasswordDTO);
    }

    @GetMapping("/confirm-forgot")
    public ResponseEntity<Void> confirmForgotPassword(
            @RequestParam("token") String token
    ){
        log.debug("confirmForgotPassword");
        String result = authenticationService.confirm_password_reset(token).getBody().getMessage();

        String redirectUrl;

        if (ErrorCode.INVALID_TOKEN.getMessage().equalsIgnoreCase(result)) {
            log.info("In failed");
            redirectUrl = "https://your-frontend.com/error?reason=confirm_failed";
        } else {
            log.info("Redirect to forgot password success");
            redirectUrl = "http://localhost:5173/?token="+token;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<User>> changePassword(
            @RequestParam("token") String token,
            @RequestBody @Valid ChangePasswordRequest request
    ){
        return authenticationService.changePassword(token,request);
    }
}
