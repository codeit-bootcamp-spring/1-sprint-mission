package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.repository.jcf.addOn.BinaryMessageRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryMessageService {
    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    private final BinaryMessageRepository repository;

    public void findById(UUID messageId){
        BinaryMessageContent findContent = repository.findById(messageId);
        // return할 땐 dto로
    }

    public void findAll(){
        List<BinaryMessageContent> findAllContent = repository.findAllByIdIn();
    }

    public void delete(UUID messageId){
        repository.delete(messageId);
    }

    public void create(BinaryContentDto dto){
        repository.save(new BinaryMessageContent(dto.getId(), dto.getBinaryContent().getBytes()));
    }

}
