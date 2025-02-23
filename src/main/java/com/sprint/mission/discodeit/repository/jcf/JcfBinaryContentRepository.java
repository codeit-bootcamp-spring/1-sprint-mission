package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JcfBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID,BinaryContent> data=new HashMap<>();


    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> result=new ArrayList<>();
        for (UUID id : ids) {
            if(data.containsKey(id)) {
                result.add(data.get(id));
            }
        }
        return result;
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
