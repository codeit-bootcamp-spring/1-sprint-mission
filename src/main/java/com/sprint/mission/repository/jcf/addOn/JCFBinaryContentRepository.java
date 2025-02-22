package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.jcf.BinarycontentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinarycontentRepository {

    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent){
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return data.values().stream()
                .filter(binaryContent ->
                        idList.contains(binaryContent.getId())).collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public void delete(UUID id){
        data.remove(id);
    }

    @Override
    public boolean existsById(UUID id){
        return data.containsKey(id);
    }
}
