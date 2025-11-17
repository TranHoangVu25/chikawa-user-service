package com.chikawa.user_service.controllers;

import com.chikawa.user_service.configuration.CustomJwtDecoder;
import com.chikawa.user_service.dto.request.AuthenticationRequest;
import com.chikawa.user_service.dto.request.IntrospectRequest;
import com.chikawa.user_service.dto.request.UserCreationRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.dto.response.AuthenticationResponse;
import com.chikawa.user_service.dto.response.IntrospectResponse;
import com.chikawa.user_service.exception.ErrorCode;
import com.chikawa.user_service.services.AuthenticationService;
import com.chikawa.user_service.services.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
        return userService.confirmUser(token);
    }

    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
    public ResponseEntity<ApiResponse<String>> login(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        try {
        AuthenticationResponse response = authenticationService.authenticate(request)
                .getBody().getResult();
        String jwt = response.getToken();
        Jwt decodedJwt = customJwtDecoder.decode(jwt);
        String scope = decodedJwt.getClaimAsString("scope");
        String full_name = decodedJwt.getClaimAsString("full_name");
        int userId = Integer.parseInt(decodedJwt.getClaimAsString("userId"));
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<String>builder()
                                .message("Login with role: "+scope)
                                .build()
                );
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<String>builder()
                                    .code(ErrorCode.ACCOUNT_PASSWORD_NOT_CORRECT.getCode())
                                    .message(ErrorCode.ACCOUNT_PASSWORD_NOT_CORRECT.getMessage())
                                    .build());
        }
    }
}
