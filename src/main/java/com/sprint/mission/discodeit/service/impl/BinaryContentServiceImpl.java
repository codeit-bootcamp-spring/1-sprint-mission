package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BinaryContentServiceImpl implements BinaryContentService {

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BinaryContent create(BinaryContentDTO.CreateBinaryContentDTO createBinaryContentDTO) {
        // User 존재 여부 확인
        if (!userRepository.existsById(createBinaryContentDTO.getUserId())) {
            throw new RuntimeException("User not found");
        }

        // 새로운 BinaryContent 생성
        BinaryContent binaryContent = new BinaryContent();
        binaryContent.setUserId(createBinaryContentDTO.getUserId());
        binaryContent.setContent(createBinaryContentDTO.getContent());
        binaryContent.setContentType(createBinaryContentDTO.getContentType());
        binaryContent.setCreatedAt(java.time.Instant.now());

        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BinaryContent not found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    @Override
    public void delete(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BinaryContent not found"));

        // BinaryContent 삭제
        binaryContentRepository.delete(binaryContent);
    }
}
