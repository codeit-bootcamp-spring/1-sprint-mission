package com.sprint.mission.discodeit.binaryContent.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
	private final BinaryContentRepository binaryContentRepository;

	/**
	 * 새로운 바이너리 컨텐츠를 생성합니다.
	 * @param request 바이너리 컨텐츠 생성 요청 정보 (작성자 ID, 메시지 ID, 파일, 컨텐츠 타입)
	 * @return 생성된 바이너리 컨텐츠 응답
	 * @throws IllegalArgumentException 작성자나 메시지가 존재하지 않는 경우
	 * @throws RuntimeException 파일 처리 실패 시
	 */
	@Override
	public BinaryContent create(BinaryContentCreateRequest request) {
		String fileName = request.fileName();
		byte[] bytes = request.bytes();
		String contentType = request.contentType();
		BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName, (long)bytes.length);
		return binaryContentRepository.save(binaryContent);
	}

	/**
	 * ID로 바이너리 컨텐츠를 조회합니다.
	 * @param id 바이너리 컨텐츠 ID
	 * @return 바이너리 컨텐츠 응답
	 * @throws IllegalArgumentException 바이너리 컨텐츠가 존재하지 않는 경우
	 */
	@Override
	public BinaryContent find(UUID id) {
		return binaryContentRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + id + " not found"));
	}

	/**
	 * ID 목록으로 여러 바이너리 컨텐츠를 조회합니다.
	 * @param ids 바이너리 컨텐츠 ID 목록
	 * @return 바이너리 컨텐츠 응답 목록
	 * @throws IllegalArgumentException 바이너리 컨텐츠가 존재하지 않는 경우
	 */
	@Override
	public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
		return binaryContentRepository.findAllByIdIn(ids);
	}

	/**
	 * 바이너리 컨텐츠를 삭제합니다.
	 * @param id 삭제할 바이너리 컨텐츠 ID
	 * @throws IllegalArgumentException 바이너리 컨텐츠가 존재하지 않는 경우
	 */
	@Override
	public void delete(UUID id) {
		binaryContentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Binary content not found"));
		binaryContentRepository.deleteById(id);
	}

}
