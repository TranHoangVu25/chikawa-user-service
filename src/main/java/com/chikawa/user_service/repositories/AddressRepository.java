package com.chikawa.user_service.repositories;

import com.chikawa.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<User,Integer> {
}
