package com.sprint.mission.discodeit.service.featureService;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    @Override
    public BinaryContent create(BinaryContentDTO binaryContentDTO) {
        try {
            BinaryContent binaryContent = new BinaryContent(binaryContentDTO);
            binaryContentRepository.save(binaryContent);
            return binaryContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(()-> new NoSuchElementException("찾을 수 없습니다."));
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> uuids) {
        List<BinaryContent> binaryContentList = new ArrayList<>();
        for(UUID id : uuids) {
            binaryContentList.add(findById(id));
        }
        return binaryContentList;
    }

    @Override
    public BinaryContent findUserProfile(UUID userId) {
        Map<UUID, BinaryContent> binaryContentMap = binaryContentRepository.findAll();
        return binaryContentMap.values().stream().filter(binaryContent -> binaryContent.getUserId().equals(userId)
                && binaryContent.getMessageId() == null).findAny().orElse(null);
    }

    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        Map<UUID, BinaryContent> binaryContentMap = binaryContentRepository.findAll();
        List<BinaryContent> binaryContentList = new ArrayList<>();
        binaryContentMap.values().stream().filter(binaryContent -> binaryContent.getMessageId() != null
                && binaryContent.getMessageId().equals(messageId)).forEach(binaryContent -> binaryContentList.add(binaryContent));
        return binaryContentList;
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);

    }

    @Override
    public void deleteUserProfile(UUID userId) {
        BinaryContent binaryContent = findUserProfile(userId);
        if(binaryContent != null){
            delete(binaryContent.getId());
        }

    }
}
