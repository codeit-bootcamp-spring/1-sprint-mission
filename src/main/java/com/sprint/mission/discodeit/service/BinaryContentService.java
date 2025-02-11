package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;

public interface BinaryContentService {
	// 바이너리 컨텐츠 생성
	BinaryContentResponse create(CreateBinaryContentRequest request);

	// ID로 조회
	BinaryContentResponse find(UUID id);

	// ID 목록으로 조회
	List<BinaryContentResponse> findAllByIdIn(List<UUID> ids);

	// 메시지 ID로 첨부파일 목록 조회
	List<BinaryContentResponse> findAllByMessageId(UUID messageId);

	//프로필 사진 변경으로 인한 삭제
	void deleteProfileImageByAuthorId(UUID authorId);

	// 삭제
	void delete(UUID id);

}
