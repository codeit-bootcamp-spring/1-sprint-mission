package com.sprint.mission.discodeit.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ValidationUtils {
    
    // TODO hibernate-validator 공식 문서 참조. 응용 하지를 못했음 => 모든 에러 잡아주는 객체 or 서비스 ? 일단 두고 각 객체에 검증 로직 추가해야함
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        String errorMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
        throw new ValidationException(String.valueOf(errorMessage));
    }
}
