package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateRequest(
        // 첨부 파일 이름
        String fileName,
        // 첨부 파일 타입
        String contentType,
        // 내용
        byte[] bytes
) {
}
