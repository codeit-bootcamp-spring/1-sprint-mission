package com.sprint.mission.repository.jcf;

import com.sprint.mission.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BinaryContentRepository {

    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    private final Map<UUID, BinaryContent> data = new HashMap<>();


    public void save(BinaryContent binaryContent){
        data.put(binaryContent.getId(), binaryContent);
    }

    public BinaryContent findById(UUID id){
        return data.get(id);
    }

    public List<BinaryContent> findAllByIdIn(){
        return new ArrayList<>(data.values());
    }

    public void delete(UUID id){
        data.remove(id);
    }
}
