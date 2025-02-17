package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent createBinaryContent(BinaryContentCreateRequest binaryContentCreateRequest) {
        // binaryContentCreateRequest 에 들어온 UUID에 따라서 BinaryContent의 메서드 이용하기
        BinaryContent binaryContent = new BinaryContent();
        binaryContentRepository.save(binaryContent);
        if( binaryContentCreateRequest.userId() != null ){
            binaryContent.saveUserProfileImage(binaryContentCreateRequest.userId());
        }
        if( binaryContentCreateRequest.messageId() != null ){
            binaryContent.attachFileToMessage(binaryContentCreateRequest.messageId());
        }
        return binaryContent;
    }

    @Override
    public BinaryContent findBinaryContentById(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseGet( () -> {
                    System.out.println(" No  + " + binaryContentId.toString() + " BinaryContent exists.\n");
                    return null;
                });
    }

    @Override
    public List<BinaryContent> findAllById() {
        return binaryContentRepository.findAll();
    }

    @Override
    public void deleteBinaryContentByUserId(UUID userId) {
        binaryContentRepository.findByUserId(userId);
    }

    @Override
    public void deleteBinaryContentByMessageId(UUID messageId) {

    }
}
