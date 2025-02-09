package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.validation.ValidateBinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final ValidateBinaryContent validateBinaryContent;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        validateBinaryContent.validateBinaryContent(request.userId(), request.messageId());

        BinaryContent binaryContent = new BinaryContent(request.userId(), request.messageId(), request.content());
        binaryContentRepository.save(binaryContent);
        return changeToDto(binaryContent);
    }

    @Override
    public BinaryContentDto findByContentId(UUID contentId) {
        BinaryContent binaryContent = binaryContentRepository.findByContentId(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Binary content not found."));
        return changeToDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllByUserId(UUID userId) {
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByUserId(userId);
        return binaryContents.stream()
                .map(BasicBinaryContentService::changeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContentDto> findByAllMessageId(UUID messageId) {
        List<BinaryContent> binaryContents = binaryContentRepository.findByAllMessageId(messageId);
        return binaryContents.stream()
                .map(BasicBinaryContentService::changeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByContentId(UUID id) {
        binaryContentRepository.deleteByContentId(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        binaryContentRepository.deleteByMessageId(messageId);
    }

    private static BinaryContentDto changeToDto(BinaryContent binaryContent) {
        return new BinaryContentDto(binaryContent.getId(), binaryContent.getUserId(), binaryContent.getMessageId(), binaryContent.getCreatedAt(), binaryContent.getContent());
    }
}
