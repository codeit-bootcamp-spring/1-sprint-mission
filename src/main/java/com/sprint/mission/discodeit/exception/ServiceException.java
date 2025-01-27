package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.error.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public ServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
