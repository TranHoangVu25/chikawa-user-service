package com.chikawa.user_service.services;

import com.chikawa.user_service.dto.request.AddressCreateRequest;
import com.chikawa.user_service.dto.request.AddressUpdateRequest;
import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.exception.ErrorCode;
import com.chikawa.user_service.models.Address;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.repositories.AddressRepository;
import com.chikawa.user_service.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository;
    UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<List<Address>>> getAllAddress() {
        List<Address> addresses = addressRepository.findAll();
        if (addresses.isEmpty()) {
            return ResponseEntity.ok()
                    .body(
                            ApiResponse.<List<Address>>builder()
                                    .message("No address found")
                                    .build()
                    );
        }
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<Address>>builder()
                                .result(addresses)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<Address>> createAddress(AddressCreateRequest request, Long userId) {
        if (userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<Address>builder()
                                    .code(ErrorCode.USER_EXISTED.getCode())
                                    .message(ErrorCode.USER_EXISTED.getMessage())
                                    .build()
                    );
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_EXISTED.getMessage()));
        Address address = new Address().builder()
                .user(user)
                .city(request.getCity())
                .locationDetail(request.getLocationDetail())
                .phoneNumber(request.getPhoneNumber())
                .recipientName(request.getRecipientName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDefaultAddress(request.getIsDefaultAddress())
                .country(request.getCountry())
                .province(request.getProvince())
                .build();
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<Address>builder()
                                .message("Successfully created address")
                                .result(addressRepository.save(address))
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<Address>> updateAddress(AddressUpdateRequest request, Long addressId, Long userId) {

        if (userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<Address>builder()
                                    .code(ErrorCode.USER_EXISTED.getCode())
                                    .message(ErrorCode.USER_EXISTED.getMessage())
                                    .build()
                    );
        } else if (addressRepository.existsById(addressId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.<Address>builder()
                                    .code(ErrorCode.ADDRESS_NOT_EXISTED.getCode())
                                    .message(ErrorCode.ADDRESS_NOT_EXISTED.getMessage())
                                    .build()
                    );
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.ADDRESS_NOT_EXISTED.getMessage()));

        address.setCity(request.getCity());
        address.setLocationDetail(request.getLocationDetail());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setRecipientName(request.getRecipientName());
        address.setUpdatedAt(LocalDateTime.now());
        address.setIsDefaultAddress(request.getIsDefaultAddress());
        address.setCountry(request.getCountry());
        address.setProvince(request.getProvince());

        return ResponseEntity.ok()
                .body(
                        ApiResponse.<Address>builder()
                                .message("Address updated successfully")
                                .result(addressRepository.save(address))
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.builder()
                                    .code(ErrorCode.ADDRESS_NOT_EXISTED.getCode())
                                    .message(ErrorCode.ADDRESS_NOT_EXISTED.getMessage())
                                    .build()
                    );
        }
        addressRepository.deleteById(id);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.builder()
                                .message("Address deleted successfully")
                                .build()
                );
    }
}
