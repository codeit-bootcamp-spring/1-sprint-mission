package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> messageList;

    public JCFMessageService() {
        this.messageList = new HashMap<>();
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 null 또는 빈 문자열일 수 없습니다.");
        }
        Message message = new Message(userId, channelId, content);
        messageList.put(message.getId(), message);
        System.out.println("메시지가 생성되었습니다: " + content);
        return message;
    }

    @Override
    public Message readMessage(UUID msgID) {
        return Optional.ofNullable(messageList.get(msgID))
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다. ID: " + msgID));
    }

    @Override
    public List<Message> readAllMessage() {
        return new ArrayList<>(messageList.values());
    }

    @Override
    public Message modifyMessage(UUID msgID, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 null 또는 빈 문자열일 수 없습니다.");
        }
        Message message = readMessage(msgID);
        String oriContent = message.getContent();
        message.updateContent(content);
        message.updateUpdatedAt();
        System.out.println("메시지가 변경되었습니다: \"" + oriContent + "\" -> \"" + content + "\"");
        return message;
    }

    @Override
    public void deleteMessage(UUID msgID) {
        Message message = readMessage(msgID);
        if (messageList.remove(msgID) != null) {
            System.out.println("메시지가 삭제되었습니다: " + message.getContent());
        } else {
            System.out.println("메시지 삭제 실패: ID " + msgID);
        }
    }
}
