package com.lamldm.identity_service.dto.request;

import com.lamldm.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
