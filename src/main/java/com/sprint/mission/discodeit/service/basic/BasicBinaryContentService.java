package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(request.userId(), request.messageId(), request.content());
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findByContentId(UUID contentId) {
        return binaryContentRepository.findByContentId(contentId);
    }

    @Override
    public BinaryContent findByUserId(UUID userId) {
        return binaryContentRepository.findByUserId(userId);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByContentId(ids);
    }

    @Override
    public void deleteByContentId(UUID id) {
        binaryContentRepository.deleteByContentId(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
    }
}
