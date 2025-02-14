package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.repository.jcf.addOn.BinaryMessageRepository;
import com.sprint.mission.dto.request.BinaryMessageContentDto;
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

    private final BinaryMessageRepository repository;


    public void create(BinaryMessageContentDto dto){
        repository.save(new BinaryMessageContent(dto));
    }

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
        if (repository.isExistById(messageId)) throw new NotFoundId();
        else repository.delete(messageId);
    }
}
