package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;

public class JCFMessageService implements MessageService {
    private final Map<String, Message> messages = new HashMap<>();

    @Override
    public void createMessage(Message message) {
        if (messages.containsKey(message.getId().toString())) {
            throw new IllegalArgumentException("이미 존재하는 메세지입니다. " + message.getId());
        }
        messages.put(message.getId().toString(), message);
        System.out.println("메세지가 생성되었습니다. " + message.getId());
    }

    @Override
    public Message readMessage(String id) {
        Message message = messages.get(id);
        if (message == null) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다. : " + id );
        }
        return message;
    }
    @Override
    public void updateMessage(Message message) {
        if (!messages.containsKey(message.getId().toString())) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다 : " + message.getId());
        }
        messages.put(message.getId().toString(), message);
        System.out.println("메세지가 업데이트되었습니다. " + message.getId());
    }
    @Override
    public void deleteMessage(String id) {
        if (!messages.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다. " + id);
        }
        messages.remove(id);
        System.out.println("메세지가 삭제되었습니다. " + id);
    }


}
