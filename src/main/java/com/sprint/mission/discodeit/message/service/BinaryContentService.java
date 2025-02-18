package com.sprint.mission.discodeit.message.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.message.dto.request.binaryContent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.message.entity.BinaryContent;

public interface BinaryContentService {
	// 바이너리 컨텐츠 생성
	BinaryContent create(CreateBinaryContentRequest request);

	// ID로 조회
	BinaryContent find(UUID id);

	// ID 목록으로 조회
	List<BinaryContent> findAllByIdIn(List<UUID> ids);

	// 메시지 ID로 첨부파일 목록 조회
	List<BinaryContent> findAllByMessageId(UUID messageId);

	//프로필 사진 변경으로 인한 삭제
	void deleteProfileImageByAuthorId(UUID authorId);

	// 삭제
	void delete(UUID id);

}
