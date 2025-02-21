package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.repository.jcf.addOn.BinaryMessageRepository;
import com.sprint.mission.dto.request.BinaryMessageContentDto;
import com.sprint.mission.common.NotFoundId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryMessageService {

    private final BinaryMessageRepository repository;


    public void create(BinaryMessageContentDto dto){
        repository.save(new BinaryMessageContent(dto));
    }

    // DTO변환은 컨트롤러에서
    public BinaryMessageContent findById(UUID messageId){
        return repository.findById(messageId).orElseThrow(NotFoundId::new);
    }


    public List<BinaryMessageContent> findAllByIn(List<UUID> messageIdList){
        List<BinaryMessageContent> binaryMessageContentList = new ArrayList<>();
        messageIdList.forEach(messageId -> repository.findById(messageId).ifPresent(binaryMessageContentList::add));
        return binaryMessageContentList;
    }

    public List<BinaryMessageContent> findAll(){
         return repository.findAll();
    }

    public void delete(UUID messageId){
        if (repository.isExistById(messageId)) throw new NotFoundId();
        else repository.delete(messageId);
    }
}
