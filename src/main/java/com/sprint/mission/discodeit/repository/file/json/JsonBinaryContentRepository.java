package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.exception.binary.BinaryContentException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        return super.save(binaryContent.getFileId(), binaryContent);
    }


    @Override
    public BinaryContent findById(UUID id) {
        checkId(id);

        return map.values().stream()
                .filter(v -> v.getFileId().equals(id))
                .findFirst()
                .orElse(null);

    }

    @Override
    public void delete(UUID id) {
        checkId(id);
        map.remove(id);

        saveToJson();
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
