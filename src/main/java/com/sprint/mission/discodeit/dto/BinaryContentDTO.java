package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class BinaryContentDTO { //파일, 이미지 등 바이너리 데이터 처리를 위한 DTO
    public static class createBinaryContentRequestDTO {
        private UUID ownerId;
        private byte[] data;
        private String contentType;
    }
    public static class BinaryContentResponseDTO {
        private String id;
        private UUID ownerId;
        private String contentType;
        private Long createdAt;
    }
}
