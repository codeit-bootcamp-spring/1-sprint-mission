package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PrivateChannelCreateRequest(

	//요구사항이 명확하지 않아 참가자가 없는 경우에도 channel을 생성할수 있을까?
	@NotEmpty(message = "참가자 목록은 비어 있을 수 없습니다.")
	List<@NotNull(message = "참가자 ID는 필수입니다.") UUID> participantIds
) {

}
