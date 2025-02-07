package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.BinaryProfileContent;
import org.springframework.stereotype.Repository;

import java.util.*;

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


    public void save(BinaryProfileContent binaryContent){
        data.put(binaryContent.getId(), binaryContent);
    }

    public BinaryProfileContent findById(UUID id){
        return data.get(id);
    }

    public List<BinaryProfileContent> findAllByIdIn(){
        return new ArrayList<>(data.values());
    }

    public void delete(UUID id){
        data.remove(id);
    }
}
