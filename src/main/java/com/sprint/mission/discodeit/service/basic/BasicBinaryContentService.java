package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

	private final BinaryContentRepository binaryContentRepository;
	private final BinaryContentMapper binaryContentMapper;
	private final BinaryContentStorage binaryContentStorage;

	@Transactional
	@Override
	public BinaryContentDto create(BinaryContentCreateRequest request) {
		log.info("Creating binary content with request: {}", request);
		try {
			String fileName = request.fileName();
			byte[] bytes = request.bytes();
			String contentType = request.contentType();
			BinaryContent binaryContent = new BinaryContent(
				fileName,
				(long)bytes.length,
				contentType
			);
			binaryContentRepository.save(binaryContent);
			binaryContentStorage.put(binaryContent.getId(), bytes);

			BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);
			log.info("Created binary content: {}", binaryContentDto);
			return binaryContentDto;
		} catch (Exception e) {
			log.error("Error creating binary content", e);
			throw e;
		}
	}

	@Override
	public BinaryContentDto find(UUID binaryContentId) {
		log.info("Finding binary content with id: {}", binaryContentId);
		return binaryContentRepository.findById(binaryContentId)
			.map(binaryContentMapper::toDto)
			.orElseThrow(() -> {
				log.warn("BinaryContent with id {} not found", binaryContentId);
				return new NoSuchElementException(
					"BinaryContent with id " + binaryContentId + " not found");
			});
	}

	@Override
	public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
		List<BinaryContentDto> binaryContentDtos = binaryContentRepository.findAllById(
				binaryContentIds).stream()
			.map(binaryContentMapper::toDto)
			.toList();
		return binaryContentDtos;
	}

	@Transactional
	@Override
	public void delete(UUID binaryContentId) {
		log.info("Deleting binary content with id: {}", binaryContentId);
		try {
			if (!binaryContentRepository.existsById(binaryContentId)) {
				log.warn("BinaryContent with id {} not found", binaryContentId);
				throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
			}
			binaryContentRepository.deleteById(binaryContentId);
			log.info("Deleted binary content with id: {}", binaryContentId);
		} catch (Exception e) {
			log.error("Error deleting binary content with id: {}", binaryContentId, e);
			throw e;
		}
	}
}
