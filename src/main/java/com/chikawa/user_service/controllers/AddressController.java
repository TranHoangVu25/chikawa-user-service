package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.request.AddressCreateRequest;
import com.chikawa.user_service.dto.request.AddressUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.models.Address;
import com.chikawa.user_service.services.AddressService;
import com.chikawa.user_service.utils.UserContextHolder;
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

    //thêm địa chỉ cho user
//    @PostMapping("/{userId}") //sau sửa = id trong jwt
    @PostMapping() //sau sửa = id trong jwt
    public ResponseEntity<ApiResponse<Address>> createUser(
            @RequestBody @Valid AddressCreateRequest request
    ) {
        try {
        Long userId = UserContextHolder.getUserId();
        return addressService.addAddress(request,userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Address>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    //lấy tất cả các address của user theo id
//    @GetMapping("/{userId}")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<Address>>> getAllUser(
//            @PathVariable Long userId
    ){
        try {
        Long userId = UserContextHolder.getUserId();
        return addressService.getAllAddressByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<Address>>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    //sửa địa chỉ
//    @PutMapping("/{userId}/{addressId}")
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> updateUser(
            @RequestBody @Valid AddressUpdateRequest request,
            @PathVariable Long addressId
    ){
        try {
        Long userId = UserContextHolder.getUserId();
        return addressService.updateAddress(request,addressId,userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Address>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    //xóa địa chỉ
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @PathVariable Long addressId
    ){
        try {
        return addressService.deleteAddress(addressId);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
