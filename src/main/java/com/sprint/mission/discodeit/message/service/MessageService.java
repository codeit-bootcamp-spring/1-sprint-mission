package com.sprint.mission.discodeit.message.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.message.dto.request.message.CreateMessageRequest;
import com.sprint.mission.discodeit.message.dto.request.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.message.dto.response.message.MessageResponse;

public interface MessageService {
	// 메시지 생성 (첨부파일 포함)
	MessageResponse create(CreateMessageRequest request);

	// 특정 채널의 메시지 목록 조회
	List<MessageResponse> findAllByChannelId(UUID channelId);

	// 메시지 조회
	MessageResponse findById(UUID messageId);

	// 메시지 수정 (첨부파일 추가 가능)
	MessageResponse update(UUID messageId, UpdateMessageRequest request);

	// 메시지 삭제 (첨부파일 포함)
	void delete(UUID messageId);
}