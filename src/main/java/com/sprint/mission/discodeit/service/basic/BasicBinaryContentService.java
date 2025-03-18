package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository repository;

    @Override
    public BinaryContent findById(UUID id) {
        BinaryContent binaryContent = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다." + id));
        return binaryContent;
    }
}
