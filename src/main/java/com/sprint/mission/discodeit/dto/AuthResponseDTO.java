package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private UUID userId;  // ✅ UUID로 변경
    private String username;

    // ✅ 기존에 String userId를 받는 생성자도 필요할 경우 추가 가능
    public AuthResponseDTO(String userId, String username) {
        // String을 UUID로 변환하는 과정에서 예외 처리
        try {
            this.userId = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            // 예외 처리 로직 추가 (필요시 Custom Exception을 던지거나 로그를 남길 수 있음)
            throw new InvalidUUIDFormatException("Invalid UUID format", e);
        }
        this.username = username;
    }

    // Custom 예외 클래스 정의 (선택 사항)
    public static class InvalidUUIDFormatException extends RuntimeException {
        public InvalidUUIDFormatException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
