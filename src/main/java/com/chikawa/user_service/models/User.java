package com.chikawa.user_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email = "";

    @Column(name = "full_name", nullable = false)
    private String fullName = "";

    @Column(nullable = false, unique = true)
    private String jti = "";

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword = "";

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "confirmation_sent_at")
    private LocalDateTime confirmationSentAt;

    @Column(name = "unconfirmed_email")
    private String unconfirmedEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String role = "customer";

    @Column(name = "line_user_id", unique = true)
    private String lineUserId;

    @Column(name = "sign_in_count")
    private Integer signInCount;

    @Column(name = "current_sign_in_at")
    private LocalDateTime currentSignInAt;

    @Column(name = "last_sign_in_at")
    private LocalDateTime lastSignInAt;

    @Column(name = "current_sign_in_ip")
    private String currentSignInIp;

    @Column(name = "last_sign_in_ip")
    private String lastSignInIp;

    @Column(name = "unlock_token", unique = true)
    private String unlockToken;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column
    private LocalDate dob;

    // Relationship
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
}

