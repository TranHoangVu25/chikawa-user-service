package com.chikawa.user_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String city;

    @Column(name = "location_detail", nullable = false, columnDefinition = "TEXT")
    private String locationDetail;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    //người nhận hàng
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_default_address", nullable = false)
    private Boolean isDefaultAddress = false;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String province;
}

