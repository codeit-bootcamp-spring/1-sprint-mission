package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    @Override
    public BinaryContent create(BinaryContentRequestDto binaryContentRequestDto) {
        return new BinaryContent(binaryContentRequestDto.data());
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return null;
    }

    @Override
    public void delete(UUID binaryContentId) {

    }
}