package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Data;
import com.sprint.mission.discodeit.service.DeleteService;

import java.util.UUID;

public class JCFDeleteService implements DeleteService {

    private final Data data;

    public JCFDeleteService(Data data) {
        this.data = data;
    }
    //UUID로 확인 후 삭제
    @Override
    public void deleteUser(UUID userUuid) {
        data.removeUser(userUuid);
    }

    @Override
    public void deleteMessage(UUID messageUuid) {
        data.removeMessage(messageUuid);
    }

    @Override
    public void deleteChannel(UUID channelUuid) {
        data.removeChannel(channelUuid);
    }
}
