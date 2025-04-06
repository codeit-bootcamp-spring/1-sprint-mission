package com.sprint.mission.discodeit.message.dto.request;

//todo 지금은 첨부파일을 받고 있지 않지만 나중에는 추가될것 같다
public record MessageUpdateRequest(
	String newContent
) {
}
