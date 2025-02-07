package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ProfileImageDTO;
import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Component;

@Component
public class BasicBinaryContentService implements BinaryContentService {

    public BinaryContent created(ProfileImageDTO data){
        BinaryContent image = new BinaryContent(data.filename(),data.fileType(),data.targetUUID(), data.data());
        return image;
    }

    //    find
    //[ ] id로 조회합니다.

    //    findAllByIdIn
    //[ ] id 목록으로 조회합니다.

    //    delete
    //[ ] id로 삭제합니다.
}
