package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentRequestDto binaryContentRequestDto) {
        return binaryContentRepository.save(new BinaryContent(binaryContentRequestDto.data()));
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return Optional.ofNullable(binaryContentRepository.find(binaryContentId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다."));
    }

    @Override
    public void delete(UUID binaryContentId) {

    }
}