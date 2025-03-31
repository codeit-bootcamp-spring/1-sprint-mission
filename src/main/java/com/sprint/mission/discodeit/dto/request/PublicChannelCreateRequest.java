package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(

	@NotBlank(message = "채널 이름은 필수입니다.")
	@Size(max = 100, message = "설명은 최대 100자까지 가능합니다.")
	String name,

	//String타입으로 최대 데이터를 받는경우 보통 255까지 사용하는걸로 알고 있는데
	//255에서 더 많이 사용하는 단위는 500인가?
	@Size(max = 500, message = "설명은 최대 500자까지 가능합니다.")
	String description
) {

}
