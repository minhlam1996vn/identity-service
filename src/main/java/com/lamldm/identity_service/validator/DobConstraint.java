package com.lamldm.identity_service.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({FIELD}) // Tùy chọn sẽ được apply ở đâu
@Retention(RUNTIME) // Sẽ đuợc xử lý lúc nào
@Constraint(validatedBy = {DobValidator.class}) // 1 constraint có thế có nhiều validator
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
