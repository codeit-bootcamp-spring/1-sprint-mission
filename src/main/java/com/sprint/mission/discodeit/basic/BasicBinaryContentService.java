package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContent create(String fileName, Long size, String contentType, byte[] data) {
        UUID id = UUID.randomUUID();

        try {
            binaryContentStorage.put(id, data);
            
            BinaryContent binaryContent = BinaryContent.builder()
                    .fileName(fileName)
                    .size(size)
                    .contentType(contentType)
                    .build();
            
            // 저장 후 ID 확인
            BinaryContent saved = binaryContentRepository.save(binaryContent);
            log.info("바이너리 콘텐츠 저장 완료: {}", saved.getId());
            return saved;
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage());
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "바이너리 콘텐츠", "id", binaryContentId));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
                .toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new ResourceNotFoundException("바이너리 콘텐츠", "id", binaryContentId);
        }
        binaryContentRepository.deleteById(binaryContentId);
    }
}
