package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentRegisterService {

    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent registerBinaryContent(UUID userId) {

        BinaryContent binaryContentToCreate = BinaryContent.createBinaryContent(userId);

        return binaryContentRepository.createBinaryContent(binaryContentToCreate);
    }
}
