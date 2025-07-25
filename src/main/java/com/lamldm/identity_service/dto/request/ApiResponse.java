package com.lamldm.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Những field nào null thì sẽ không hiển thị
public class ApiResponse<T> {
    int code = 1000;
    String message;
    T result;
}
