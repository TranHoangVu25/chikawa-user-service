package com.chikawa.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    private String email = "";
    private String fullName = "";
    private String jti = "";
    private String encryptedPassword = "";
    private String confirmationToken;
    private LocalDateTime confirmedAt;
    private LocalDateTime confirmationSentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role = "customer";
    private Integer signInCount;
    private LocalDateTime currentSignInAt;
    private LocalDate dob;
}
