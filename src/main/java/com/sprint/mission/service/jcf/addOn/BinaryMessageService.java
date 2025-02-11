package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.repository.jcf.addOn.BinaryMessageRepository;
import com.sprint.mission.service.dto.request.BinaryMessageContentDto;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public BinaryMessageContentDto findById(UUID messageId){
        return repository.findById(messageId)
                .map((bmc) -> new BinaryMessageContentDto(bmc))
                .orElseThrow(NotFoundId::new);
    }

    public List<BinaryMessageContentDto> findAll(){
         return repository.findAll().stream()
                 .map((bmc) -> new BinaryMessageContentDto(bmc))
                 .collect(Collectors.toCollection(ArrayList::new));
    }

    public void delete(UUID messageId){
        repository.delete(messageId);
    }

    public void create(BinaryMessageContentDto dto){
        repository.save(new BinaryMessageContent(dto));
    }
}
