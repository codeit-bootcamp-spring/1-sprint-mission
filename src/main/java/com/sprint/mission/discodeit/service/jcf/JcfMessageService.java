package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

import static com.sprint.mission.discodeit.util.MyLogger.log;

public class JcfMessageService implements MessageService {
    private final Map<UUID, Message> data=new HashMap<>();
//    private final List<Message> data=new ArrayList<>();

    @Override
    public void messageSave(Message message) {
        if (message.getContent().trim().isEmpty()) {
            log("메세지 내용을 입력해주세요.");
            return;
        }
        data.put(message.getId(),message);
    }

    @Override
    public Optional<Message> findMessage(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAllMessages() {
        Message copyMessage=null;
        List<Message> newMessageList=new ArrayList<>();
       //깊은복사
        for (UUID uuid : data.keySet()) {
            copyMessage=data.get(uuid);
            newMessageList.add(copyMessage);
        }
        return newMessageList;
    }

    @Override
    public void updateMessage(UUID id,String updateMessage) {
        if(data.containsKey(id)) {
            data.get(id).updateMessage(updateMessage);
            log("메세지 업데이트 완료");
        }else {
            log("메세지 ID가 유효하지 않습니다");
        }
    }


    @Override
    public void deleteMessage(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
        }
    }
    private static Message copyMessage(Message message) {
        Message copyMessage = new Message(message);
        return copyMessage;
    }
}
