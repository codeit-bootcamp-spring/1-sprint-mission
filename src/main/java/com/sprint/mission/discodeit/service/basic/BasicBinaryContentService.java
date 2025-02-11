package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ProfileImageDTO;
import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BasicBinaryContentService implements BinaryContentService {

    public BinaryContent created(ProfileImageDTO data){
        BinaryContent binaryContent =
                new BinaryContent(data.contentType(),
                        data.targetUUID(), data.filename(),data.fileType(), data.data());
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> find(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<BinaryContent> findAllByIdIn() {
        return List.of();
    }

    @Override
    public void delete() {

    }

    //    find
    //[ ] id로 조회합니다.

    //    findAllByIdIn
    //[ ] id 목록으로 조회합니다.

    //    delete
    //[ ] id로 삭제합니다.
}
