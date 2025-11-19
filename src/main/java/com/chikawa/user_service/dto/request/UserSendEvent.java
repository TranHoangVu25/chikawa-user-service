package com.chikawa.user_service.dto.request;

import com.chikawa.user_service.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSendEvent {
    private Long id;
    private String email;
    private String fullName;
    private String lineUserId;
    private Integer monthOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Action action;
}
