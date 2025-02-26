package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
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
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        BinaryContent content = null;
        for (BinaryContent binaryContent : data.values()) {
            if (binaryContent.getType() == BinaryContent.Type.PROFILE
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
            if (binaryContent.getType() == BinaryContent.Type.MESSAGE
                    && binaryContent.getId().equals(messageId)) {
                contents.add(binaryContent);
            }
        }

        return contents;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
