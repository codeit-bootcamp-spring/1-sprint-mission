package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContentMap;
    JCFBinaryContentRepository(){
        binaryContentMap = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        if (binaryContent==null){
            throw new IllegalArgumentException("BinaryContent isn't delivered");
        }
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        BinaryContent binaryContent = binaryContentMap.get(id);
        if (binaryContent == null) {
            throw new IllegalArgumentException("Cannot find Object");
        }
        return binaryContent;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> uuidList) {
        List<BinaryContent> findAllByIdList = new ArrayList<>();
        for (UUID id : uuidList) {
            BinaryContent binaryContent = findById(id);
            findAllByIdList.add(binaryContent);
        }
        return findAllByIdList;
    }

    @Override
    public Map<UUID, BinaryContent> load() {
        if (binaryContentMap.isEmpty()) {
            throw new IllegalArgumentException("Cannot find Object");
        }
        return binaryContentMap;
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is null");
        }
        binaryContentMap.remove(id);
    }

}
