package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Optional<BinaryContent> getBinaryContent(UUID id) {
        return binaryContentRepository.getBinaryContentById(id);
    }

    @Override
    public BinaryContent saveBinaryContent(BinaryContent binaryContent) {
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    @Override
    public List<BinaryContent> getBinaryContentListByIds(List<UUID> ids) {
        return binaryContentRepository.getBinaryContentListByIds(ids);
    }
}
