package com.sprint.mission.discodeit.service.featureBasicService;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    @Override
    public BinaryContent create(BinaryContentCreateDto binaryContentDTO) {
        String fileName = binaryContentDTO.fileName();
        byte[] bytes = binaryContentDTO.bytes();
        String contentType = binaryContentDTO.contentType();
        BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException(binaryContentId + "를 찾을 수 없습니다."));
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds)
                .stream().collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException(id + "를 찾을 수 없습니다.");
        }
        binaryContentRepository.deleteById(id);
    }
}
