package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.BinaryProfileContent;
import com.sprint.mission.repository.jcf.addOn.BinaryProfileRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryProfileService {
    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    private final BinaryProfileRepository repository;


    public void create(BinaryContentDto dto){
        repository.save(new BinaryProfileContent(dto.getId(), dto.getBytes()));
    }

    public void findById(UUID messageId){
        BinaryProfileContent findContent = repository.findById(messageId);
        // return할 땐 dto로
    }

    public void findAll(){
        List<BinaryProfileContent> findAllContent = repository.findAllByIdIn();
    }

    public void delete(UUID messageId){
        repository.delete(messageId);
    }


}
