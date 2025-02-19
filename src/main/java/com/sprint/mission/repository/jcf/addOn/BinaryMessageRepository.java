package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BinaryMessageRepository {

    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    // KEY : messageId
    private final Map<UUID, BinaryMessageContent> data = new HashMap<>();

    public void save(BinaryMessageContent binaryContent){
        data.put(binaryContent.getId(), binaryContent);
    }

    public Optional<BinaryMessageContent> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    public List<BinaryMessageContent> findAll(){
        return new ArrayList<>(data.values());
    }

    public void delete(UUID messageId){
        data.remove(messageId);
    }

    public Boolean isExistById(UUID userId){
        return data.get(userId) != null;
    }
}
