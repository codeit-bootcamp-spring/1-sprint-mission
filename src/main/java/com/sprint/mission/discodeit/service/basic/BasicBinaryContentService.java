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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final ValidateBinaryContent validateBinaryContent;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
//        validateBinaryContent.validateBinaryContent(request.userId(), request.messageId());
        String fileName = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();
        BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, request.bytes());
        BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
        return changeToDto(createdBinaryContent);
    }

    @Override
    public BinaryContentDto findById(UUID contentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Binary content not found."));
        return changeToDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        return binaryContents.stream()
                .map(BasicBinaryContentService::changeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    private static BinaryContentDto changeToDto(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType(),
                binaryContent.getBytes(),
                binaryContent.getCreatedAt());
    }
}
