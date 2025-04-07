package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.error.ErrorResponse;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;


@Mapper
public interface ErrorResponseMapper {
    ErrorResponseMapper INSTANCE = Mappers.getMapper(ErrorResponseMapper.class);

    @Mapping(target = "timestamp", expression = "java(java.time.Instant.now())")
    @Mapping(target = "code", source = "errorCode")
    @Mapping(target = "message", source = "errorCode.message")
    @Mapping(target = "status", source = "errorCode.status")
    @Mapping(target = "exceptionType", expression = "java(ex.getClass().getSimpleName())")
    @Mapping(target = "details", expression = "java(extractDetails(ex))")
    ErrorResponse fromValidationException(MethodArgumentNotValidException ex, ErrorCode errorCode);

    @Mapping(target = "timestamp", expression = "java(java.time.Instant.now())")
    @Mapping(target = "code", source = "errorCode")
    @Mapping(target = "message", source = "errorCode.message")
    @Mapping(target = "status", source = "errorCode.status")
    ErrorResponse fromException(Exception ex, ErrorCode errorCode);

    // 커스텀 메서드: FieldError 리스트 → Map<String, Object>
    default Map<String, Object> extractDetails(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
    }
}
