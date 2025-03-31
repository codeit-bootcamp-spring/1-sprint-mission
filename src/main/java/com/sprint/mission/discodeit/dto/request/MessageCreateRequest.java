package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageCreateRequest(
	@NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
	String content,
	@NotNull(message = "채널 ID는 필수입니다.")
	UUID channelId,
	@NotNull(message = "작성자 ID는 필수입니다.")
	UUID authorId
) {

}
