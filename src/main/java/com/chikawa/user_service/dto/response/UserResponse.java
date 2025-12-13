package com.chikawa.user_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String email;
    String fullName;
    String role;
    String lineUserId;
    LocalDateTime lockedAt;
    LocalDateTime createdAt;
    LocalDateTime lastSignInAt;
}
