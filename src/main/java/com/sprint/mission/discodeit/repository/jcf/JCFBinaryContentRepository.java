package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BelongType;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        data = new HashMap<>(100);
    }

    @Override
    public BinaryContent save(BinaryContent content) {
        data.put(content.getId(), content);
        return content;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 BinaryContent 입니다."));
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        BinaryContent content = null;
        for (BinaryContent binaryContent : data.values()) {
            if (binaryContent.getType() == BelongType.PROFILE
                    && binaryContent.getBelongTo().equals(userId)) {
                content = binaryContent;
            }
        }

        return Optional.ofNullable(content);
    }

    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        List<BinaryContent> contents = new ArrayList<>(100);

        for (BinaryContent binaryContent : data.values()) {
            if (binaryContent.getType() == BelongType.MESSAGE
                    && binaryContent.getId().equals(messageId)) {
                contents.add(binaryContent);
            }
        }

        if (contents.isEmpty()) {
            throw new NotFoundException("존재하지 않는 message에 대한 BinaryContent 요청.");
        }
        return contents;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
