package com.sprint.mission.discodeit.message.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.entity.Message;

public interface MessageService {
	// 메시지 생성 (첨부파일 포함)
	Message create(MessageCreateRequest messageCreateRequest,
		List<BinaryContentCreateRequest> binaryContentCreateRequests);

	// 메시지 조회
	Message find(UUID messageId);

	// 특정 채널의 메시지 목록 조회
	List<Message> findAllByChannelId(UUID channelId);

	// 메시지 수정 (첨부파일 추가 가능)
	Message update(UUID messageId, MessageUpdateRequest request);

	// 메시지 삭제 (첨부파일 포함)
	void delete(UUID messageId);
}