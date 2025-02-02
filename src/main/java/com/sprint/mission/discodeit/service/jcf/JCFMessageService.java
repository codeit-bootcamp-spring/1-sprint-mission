package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID,Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }

    // Create Message
    @Override
    public Message createMessage(UUID channelId, UUID authorId, String content) {
        if(isContent(content)){
            Message newMessage = new Message(content,channelId,authorId);
            data.put(newMessage.getId(), newMessage);
            return newMessage;
        }
        return null;
    }

    // Read all message
    @Override
    public List<Message> getAllMessageList() {
        return new ArrayList<>(data.values());
    }

    // id 받아서 -> Read that message
    @Override
    public Message searchById(UUID messageId) {
        Message message = data.get(messageId);
        if(message == null) {
            System.out.println("메세지가 없습니다.");
        }
        return message;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = data.remove(messageId);
        if(message == null) {
            System.out.println("삭제할 메시지가 존재하지 않습니다.");
        } else {
            System.out.println("메시지가 성공적으로 삭제되었습니다 : " + message.getContent());
        }
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = data.get(messageId);
        if(message == null) {
            System.out.println("수정할 메시지가 존재하지 않습니다.");
            return;
        }
        if(isContent(content)) {
            message.setContent(content);
            System.out.println("메시지가 성공적으로 수정되었습니다. : " + content);
        }

    }


    // 유효성 검사 -> message에 내용이 들어가있는지?
    private boolean isContent(String content) {
        if (content.isBlank()) {
            System.out.println("내용을 입력해주세요!!!");
            return false;
        }
        return true;
    }
}