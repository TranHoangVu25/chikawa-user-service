package com.chikawa.user_service.repositories;

import com.chikawa.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmationToken(String token);

    Optional<User> findByLineUserId(String lineUserId);

    boolean existsByLineUserId(String lineUserId);


}
