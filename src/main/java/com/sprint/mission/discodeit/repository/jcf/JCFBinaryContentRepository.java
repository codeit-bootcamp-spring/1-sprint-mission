package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.config.JCFRepositoryCondition;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@NoArgsConstructor
@JCFRepositoryCondition
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContentData = new HashMap<>();

    @Override
    public BinaryContent saveBinaryContent(BinaryContent binaryContent) {
        binaryContentData.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findBinaryContentById(UUID binaryContentId) {
        return binaryContentData.get(binaryContentId);
    }

    @Override
    public List<BinaryContent> findAllBinaryContentById(List<UUID> binaryContentIdList) {
        List<BinaryContent> binaryContentList = new ArrayList<>();

        for (UUID binaryContentId : binaryContentIdList) {
            binaryContentList.add(binaryContentData.get(binaryContentId));
        }
        return binaryContentList;
    }

    @Override
    public void removeBinaryContent(UUID binaryContentId) {
        binaryContentData.remove(binaryContentId);
    }
}
