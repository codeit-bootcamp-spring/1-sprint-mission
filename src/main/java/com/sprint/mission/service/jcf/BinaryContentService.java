package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.BinaryContent;
import com.sprint.mission.repository.jcf.BinaryContentRepository;
import com.sprint.mission.service.dto.request.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    //create
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.
    //find
    //[ ] id로 조회합니다.
    //findAllByIdIn
    //[ ] id 목록으로 조회합니다.
    //delete
    //[ ] id로 삭제합니다.

    private final BinaryContentRepository repository;

    public void create(BinaryContentDto dto){
        if (dto.getMessageId() == null && dto.getUserId() != null){
            BinaryContent userProfile = BinaryContent.createUserProfile(dto.getUserId(), dto.getBytes());
            repository.save(userProfile);
        } else if (dto.getMessageId() != null && dto.getUserId() == null){
            BinaryContent messageContent = BinaryContent.createMessageContent(dto.getMessageId(), dto.getBytes());
            repository.save(messageContent);
        }
    }



}
