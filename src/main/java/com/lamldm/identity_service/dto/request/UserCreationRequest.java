package com.lamldm.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.lamldm.identity_service.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID") // Key của errorCode
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB") // Kiểm tra tối thiểu 18 tuổi
    LocalDate dob;
}
