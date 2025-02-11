package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class BinaryContentDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateBinaryContentDTO {
        private UUID userId;  // 해당 바이너리를 업로드한 사용자
        private byte[] content;  // 바이너리 데이터
        private String contentType;  // 파일 타입 (예: 이미지, PDF 등)
    }
}
