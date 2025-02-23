package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    public BasicBinaryContentService(BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public BinaryContent createBinaryContent(BinaryContent binaryContent) {
        log.info("BinaryContent를 생성합니다: {}", binaryContent.getId());
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findBinaryContentById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID로 BinaryContent를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<BinaryContent> findAllFiles(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteFile(UUID id) {
        log.info("BinaryContent를 삭제합니다: {}", id);
        binaryContentRepository.deleteById(id);
    }
}
