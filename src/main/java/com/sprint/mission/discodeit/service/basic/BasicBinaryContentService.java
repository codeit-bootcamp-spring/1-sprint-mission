package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent created(BinaryContentDTO data){
        BinaryContent binaryContent =
                new BinaryContent(data.contentType(),
                        data.targetUUID(), data.filename(),data.fileType(), data.data());
        binaryContentRepository.save(binaryContent, data.contentType());
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
