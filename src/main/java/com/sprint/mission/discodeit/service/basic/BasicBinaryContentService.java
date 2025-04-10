package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.file.FileNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository repository;

    @Override
    public BinaryContent findById(UUID id) {
        log.debug("파일 조회 시도 - id: {}", id);
        BinaryContent binaryContent = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("파일 조회 실패 - 존재하지 않거나 삭제된 ID : {}", id);
                    return new FileNotFoundException(id);
                });
        log.info("파일 조회 성공 - id: {}", id);
        return binaryContent;
    }
}
