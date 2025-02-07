package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentRepository {
	BinaryContent save(BinaryContent binaryContent);

	Optional<BinaryContent> findById(UUID id);

	//사용자가 업로드한 모든 바이너리 파일 목록 조회
	List<BinaryContent> findByAuthorId(UUID authorId);

	//특정 메시지에 첨부된 파일 조회
	Optional<BinaryContent> findByMessageId(UUID messageId);

	//사용자의 전체 업로드 파일 이력 조회
	List<BinaryContent> findAllByAuthorId(UUID authorId);

	void deleteById(UUID id);
}
