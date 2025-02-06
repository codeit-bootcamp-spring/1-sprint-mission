package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BinaryContentServiceImpl implements BinaryContentService {

    private BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent creat(BinaryContentCreateRequest request) {
        return null;
    }

    @Override
    public Optional<BinaryContent> find(UUID id) {
        return binaryContentRepository.findById(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(UUID id) {
        binaryContentRepository.deleteByUserId(id);
    }
}
