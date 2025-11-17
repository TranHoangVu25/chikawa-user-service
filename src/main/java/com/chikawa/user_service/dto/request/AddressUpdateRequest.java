package com.chikawa.user_service.dto.request;

import com.chikawa.user_service.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateRequest {
    private String city;
    private String locationDetail;
    private String phoneNumber;
    private String recipientName;
    private LocalDateTime updatedAt;
    private Boolean isDefaultAddress;
    private String country;
    private String province;
}
