package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.validation.BinaryContentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentValidator binaryContentValidator;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();

        BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, request.bytes());
        BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
        return binaryContentMapper.binaryContentEntityToDto(createdBinaryContent);
    }

    @Override
    public BinaryContentDto findById(UUID contentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Binary content not found: " + contentId));
        return binaryContentMapper.binaryContentEntityToDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        return binaryContents.stream()
                .map(binaryContentMapper::binaryContentEntityToDto)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
