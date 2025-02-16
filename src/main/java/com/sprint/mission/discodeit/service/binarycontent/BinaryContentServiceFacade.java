package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDeleteResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentRegisterResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentSearchResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceFacade {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentRegisterService registerService;
    private final BinaryContentDeleteService deleteService;

    public BinaryContentRegisterResponse registerBinaryContent() {

        BinaryContent BinaryContent = registerService.registerBinaryContent();

        return BinaryContentRegisterResponse.from(BinaryContent);
    }

    public BinaryContentSearchResponse searchBinaryContentById(UUID BinaryContentId) {

        BinaryContent BinaryContentById =
            binaryContentRepository.findBinaryContentById(BinaryContentId);

        return BinaryContentSearchResponse.from(BinaryContentById);
    }

    public List<BinaryContent> searchAllBinaryContent(List<UUID> binaryContentIds) {

        return binaryContentRepository.findAllBinaryContentByIdIn(binaryContentIds);
    }

    public BinaryContentDeleteResponse deleteById(UUID binaryContentId) {

        BinaryContent deleteBinaryContentById = deleteService.deleteById(binaryContentId);

        return BinaryContentDeleteResponse.from(deleteBinaryContentById);
    }
}
