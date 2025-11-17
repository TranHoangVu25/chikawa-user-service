package com.chikawa.user_service.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    //dành cho token , tọa token phải có jti  lúc logout thì huủy những token đang tồn tại
    //lúc taojnusser gán 1 jti bấtkyfyf, chuỗi random 64bit tạo token gán jti vào token
    //khi check valid thì check jti trong jwwt có = trong db k
    // khi log out đổi jti thành 1 cái khác
    //monolithic
    @Column(nullable = false, unique = true)
    private String jti = "";

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword = "";

    //lúc người dùng đăng ký, taạo ra 1 cái này, khi gửi mial về kèm token trong đường dẫn,
    //người dùng click vào button và server xem có trùng với trong db không trường ở dưới set time
    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "confirmation_sent_at")
    private LocalDateTime confirmationSentAt;

    //thừa
    @Column(name = "unconfirmed_email")
    private String unconfirmedEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String role = "CUSTOMER";

    //id trong line
    @Column(name = "line_user_id", unique = true)
    private String lineUserId;

    @Column(name = "sign_in_count")
    private Integer signInCount;

    @Column(name = "current_sign_in_at")
    private LocalDateTime currentSignInAt;

    @Column(name = "last_sign_in_at")
    private LocalDateTime lastSignInAt;

    //check ip
    @Column(name = "current_sign_in_ip")
    private String currentSignInIp;

    @Column(name = "last_sign_in_ip")
    private String lastSignInIp;

    //k dùng
    @Column(name = "unlock_token", unique = true)
    private String unlockToken;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column
    private LocalDate dob;

    // Relationship
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private List<Address> addresses;
}

