package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonBinaryContentRepository extends JsonRepository<UUID, BinaryContent> implements BinaryContentRepository {

    public JsonBinaryContentRepository(RepositoryProperties properties) {
        super(
                new ObjectMapper().registerModule(new JavaTimeModule()),
                properties,
                "binaryContent.json",
                new TypeReference<HashMap<UUID, BinaryContent>>() {}
        );
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        if (binaryContent == null) {
            throw new BinaryContentException("save 실패했습니다.");
        }

        return super.save(binaryContent.getId(), binaryContent);
    }

    /**
     * @param messageId
     * @return
     * @Description: 메세지에 있는 첨부파일 찾기
     */
    @Override
    public BinaryContent findByMessageId(UUID messageId) {
        return map.values().stream()
                .filter(v -> v.getMessageId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new BinaryContentException("찾는 파일이 없습니다."));
    }

    /**
     * @param userId
     * @return
     * @Description: 유저가 보냈었던 파일을 찾기
     */
    @Override
    public Map<UUID, BinaryContent> findByUserId(UUID userId) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userId))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    @Override
    public BinaryContent findById(UUID id) {
        checkId(id);

        return map.values().stream()
                .filter(v -> v.getUserId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BinaryContentException("찾는 binaryConetent가 없습니다."));

    }

    @Override
    public void remove(UUID id) {
        checkId(id);

        map.remove(id);
    }

    /**
     * @param userId
     * @return
     * @Description: 유저가 보낸 모든 파일 삭제
     */
    @Override
    public void removeAllContentOfUser(UUID userId) {
        map.entrySet().removeIf(entry ->
                entry.getValue().getUserId().equals(userId));
        saveToJson();
    }

    /**
     * @param messageId
     * @return
     * @Description: 유저가 보낸 메세지의 파일 삭제
     */
    @Override
    public Map<UUID, BinaryContent> removeContent(UUID messageId) {
        map.entrySet().removeIf(entry ->
                entry.getValue().getMessageId().equals(messageId));

        saveToJson();
        return findAll();
    }

    @Override
    public Map<UUID, BinaryContent> findAll() {
        return map;
    }

    private static void checkId(UUID id) {
        if (id == null) {
            throw new BinaryContentException("id가 유효하지 않습니다.");
        }
    }

}
