package com.chikawa.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @NotBlank @Email
    private String email;
    @NotBlank
    private String fullName;
    private String jti;
    @NotBlank
    private String encryptedPassword;
    private String confirmationToken;
    private LocalDateTime confirmedAt;
    private LocalDateTime confirmationSentAt;
    private String role = "customer";
    private Integer signInCount;
    private LocalDateTime currentSignInAt;
    private LocalDate dob;
}
