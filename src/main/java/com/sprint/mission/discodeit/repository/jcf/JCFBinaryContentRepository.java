package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<String, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(createBinaryContentDto.binaryImage(), createBinaryContentDto.createdAt());
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(String id) {
        return data.get(id);
    }

    @Override
    public List<BinaryContent> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean delete(String id) {
        return data.remove(id) != null;
    }
}
