package com.sprint.mission.discodeit.dto.request;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes // 파일,이미지,동영상 같은 바이트 단위의 바이너리 데이터를 저장하는 배열
) {
}
