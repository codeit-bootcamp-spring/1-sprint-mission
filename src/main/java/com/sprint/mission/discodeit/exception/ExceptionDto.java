package com.sprint.mission.discodeit.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "에러 응답 DTO")
public class ExceptionDto {
    @Schema(description = "HTTP 상태 코드", example = "400")
    private final HttpStatus httpCode;

    @Schema(
            description = "에러 코드",
            example = "VALID0001"
    )
    private final String code;

    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
    private final String message;

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}
