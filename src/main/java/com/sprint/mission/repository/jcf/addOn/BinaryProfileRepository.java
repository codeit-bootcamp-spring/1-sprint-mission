package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryProfileContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class BinaryProfileRepository {

    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    private final Map<UUID, BinaryProfileContent> data = new HashMap<>();

    public BinaryProfileContent save(BinaryProfileContent binaryContent){
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    public Optional<BinaryProfileContent> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    public List<BinaryProfileContent> findAll(){
        return new ArrayList<>(data.values());
    }

    public void delete(UUID id){
        data.remove(id);
        log.info("[Remove BinaryProfile]");
    }
}
