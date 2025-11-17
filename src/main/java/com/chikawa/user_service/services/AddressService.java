package com.chikawa.user_service.services;

import com.chikawa.user_service.dto.request.AddressCreateRequest;
import com.chikawa.user_service.dto.request.AddressUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.models.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    ResponseEntity<ApiResponse<List<Address>>> getAllAddressByUserId(Long userId);

    ResponseEntity<ApiResponse<Address>> addAddress(AddressCreateRequest request, Long userId);

    ResponseEntity<ApiResponse<Address>> updateAddress(AddressUpdateRequest request, Long addressId, Long userId);

    ResponseEntity<ApiResponse<?>> deleteAddress(Long id);
}
