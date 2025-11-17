package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.request.AddressCreateRequest;
import com.chikawa.user_service.dto.request.AddressUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.models.Address;
import com.chikawa.user_service.services.AddressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("api/v1/address")
public class AddressController {
    AddressService addressService;

    @PostMapping("/{userId}") //sau sửa = id trong jwt
    public ResponseEntity<ApiResponse<Address>> createUser(
            @RequestBody @Valid AddressCreateRequest request,
            @PathVariable Long userId
    ) {
        return addressService.addAddress(request,userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Address>>> getAllUser(
            @PathVariable Long userId
    ){
        return addressService.getAllAddressByUserId(userId);
    }

    @PutMapping("/{userId}/{addressId}")
    public ResponseEntity<ApiResponse<Address>> updateUser(
            @RequestBody @Valid AddressUpdateRequest request,
            @PathVariable Long userId,
            @PathVariable Long addressId
    ){
        return addressService.updateAddress(request,addressId,userId);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @PathVariable Long addressId
    ){
        return addressService.deleteAddress(addressId);
    }
}
