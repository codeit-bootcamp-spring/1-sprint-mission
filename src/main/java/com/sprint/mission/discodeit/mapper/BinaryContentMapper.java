package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

    public BinaryContentDto toDto(BinaryContent entity) {
        if (entity == null) {
            return null;
        }

        return new BinaryContentDto(
                entity.getId(),
                entity.getFileName(),
                entity.getSize(),
                entity.getContentType()
        );
    }
}
