package com.chikawa.user_service.repositories;

import com.chikawa.user_service.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    boolean existsByUserId(Long userId);
    Address findByUserId(Long userId);
}
