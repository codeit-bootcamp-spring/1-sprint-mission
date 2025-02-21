package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDeleteResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentDeleteService {

    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent deleteById(UUID binaryContentId) {

        return binaryContentRepository.deleteBinaryContentById(binaryContentId);
    }
}
