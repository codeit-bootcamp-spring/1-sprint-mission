package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryProfileContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class BinaryProfileRepository {

    private final Map<UUID, BinaryProfileContent> data = new HashMap<>();

    public void save(BinaryProfileContent binaryContent){
        data.put(binaryContent.getId(), binaryContent);
    }

    public Optional<BinaryProfileContent> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    public List<BinaryProfileContent> findAll(){
        return new ArrayList<>(data.values());
    }

    public Optional<BinaryProfileContent> delete(UUID id){
        return Optional.ofNullable(data.remove(id));
    }

    public Boolean isExistById(UUID userId){
        return data.get(userId) != null;
    }
}
