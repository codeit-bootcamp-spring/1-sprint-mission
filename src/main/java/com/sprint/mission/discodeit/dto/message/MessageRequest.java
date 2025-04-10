package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.aspectj.bridge.IMessage;

public record MessageRequest(
    @NotNull(message = "메시지 내용을 입력하세요.")
    String content,
    @NotBlank(message = "채널 ID는 필수입니다.")
    UUID channelId,
    @NotBlank(message = "유저 ID는 필수입니다.")
    UUID userId,
    @Valid
    List<BinaryContent> attachments
) {

}
