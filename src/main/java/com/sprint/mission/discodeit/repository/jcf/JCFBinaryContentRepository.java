package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.ContentType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<Long, BinaryContent> data;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long save(BinaryContent binaryContent) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, binaryContent);
        return id;
    }

    @Override
    public BinaryContent load(Long id) {
        return data.get(id);

    }

    @Override
    public Map<Long, BinaryContent> loadAll() {
        return data;
    }

    @Override
    public void delete(Long id) {
        if (data.remove(id) == null) {
            throw new CustomException(ErrorCode.FAILED_TO_DELETE_DATA);
        }
    }

    @Override
    public boolean hasBinaryContent(UUID referenceId) {
        return loadAll().entrySet().stream()
                .anyMatch(entry -> entry.getValue().getReferenceId().equals(referenceId));
    }

    @Override
    public Map.Entry<Long, BinaryContent> findBinaryContentByUUID(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                // TODO: ErrorCode 추가해야 함
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Map.Entry<Long, BinaryContent> findProfileImageByMessageId(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getReferenceId().equals(uuid)
                        && entry.getValue().getContentType().equals(ContentType.PROFILE))
                .findFirst()
                // TODO: ErrorCode 추가해야 함
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Map<Long, BinaryContent> findMessageImageByMessageId(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getReferenceId().equals(uuid)
                        && entry.getValue().getContentType().equals(ContentType.PICTURE))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAllFileByReferenceId(UUID referenceId) {
        loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getReferenceId().equals(referenceId))
                .map(Map.Entry::getKey)
                .forEach(this::delete);
    }
}
