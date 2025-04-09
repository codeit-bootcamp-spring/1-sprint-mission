package com.sprint.mission.discodeit.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProfileFileValidator.class)
public @interface ProfileFile {
    String message() default "이미지 파일만 업로드할 수 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}