package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class DiscodeitException extends RuntimeException{

    // TODO 질문 : 왜 여기에 final이 요구되는지? 그러면 기본생성자 못쓰잖아
    protected Instant timestamp;
    protected ErrorCode errorCode;
    protected Map<String, Object> details = new HashMap<>();

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details){
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = (details != null) ? details : new HashMap<>(); // NPE 방지
    }
}
