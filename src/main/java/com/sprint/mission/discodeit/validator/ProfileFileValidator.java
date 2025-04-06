package com.sprint.mission.discodeit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProfileFileValidator implements ConstraintValidator<ProfileFile, MultipartFile> {

    private final List<String> allowedTypes = List.of("image/jpg", "image/jpeg", "image/png", "image/gif", "image/webp");

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // 값이 없으면 통과 (필수 아님)
        if (value == null || value.isEmpty()) {
            return true;
        }

        return allowedTypes.contains(value.getContentType());
    }
}