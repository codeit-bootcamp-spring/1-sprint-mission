package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MessageRequest {

    @Getter
    @NoArgsConstructor
    public static class Create {

        @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
        private String content;

        @NotNull(message = "채널 ID는 필수입니다.")
        private UUID channelId;

        @NotNull(message = "사용자 ID는 필수입니다.")
        private UUID authorId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        @NotBlank(message = "수정할 메시지 내용은 비어 있을 수 없습니다.")
        private String newContent;
    }
}
