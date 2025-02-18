package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() { this.data = new HashMap<>(); }

    @Override
    public BinaryContent findById(UUID id) {
        return data.get(id);
    }

    @Override
    public BinaryContent save(MultipartFile file, UUID userId) {
        try {
            BinaryContent binaryContent = new BinaryContent(userId, file.getOriginalFilename(), file.getContentType(), file.getBytes());
            data.put(binaryContent.getId(), binaryContent);
            return binaryContent;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }
}
