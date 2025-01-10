package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.MyLog;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;


public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageRepository;
    private final UserService userService;

    public JCFMessageService(UserService userService) {
        this.messageRepository = new HashMap<>();
        this.userService = userService;
    }

    @Override
    public MyLog<Message> createMessage(String content, User fromUser, User toUser) {
        if (content.isEmpty()) {
            return new MyLog<>(null, "메시지 내용을 입력해주세요!");
        }
        List<User> users = userService.getAllUser();
        if (!(users.contains(fromUser) && users.contains(toUser))) {
            return new MyLog<>(null, "송수신자를 다시 확인해주세요");
        }
        Message message = new Message(content, fromUser, toUser);
        messageRepository.put(message.getId(), message);
        return new MyLog<>(message, "메시지 생성이 완료되었습니다");
    }

    // 메시지를 보낸 회원이 메시지 조회하기
    @Override
    public MyLog<List<Message>> getMessageByUser(User fromUser, User toUser) {
        List<Message> messages = new ArrayList<>();
        for (Message message : messageRepository.values()) {
            if (message.getFromUser().equals(fromUser) && message.getToUser().equals(toUser)) {
                messages.add(message);
            }
        }
        if (!messages.isEmpty()) {
            return new MyLog<>(messages, "메시지 조회가 완료되었습니다");
        }

        return new MyLog<>(null, "메시지가 조회되지 않습니다");
    }

    @Override
    public MyLog<Message> updateMessageId(UUID id, String newContent) {
        if (messageRepository.get(id) != null) {
            Message message = messageRepository.get(id);
            message.update(newContent);
            return new MyLog<>(message, "업데이트가 완료되었습니다");
        }
        return new MyLog<>(null, "존재하지 않는 메시지입니다");
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messageRepository.values());
    }

    @Override
    public MyLog<Message> deleteMessage(Message message) {
        if (messageRepository.get(message.getId()) != null) {
            messageRepository.remove(message.getId());
            return new MyLog<>(message, "해당 메시지를 삭제하였습니다");
        }
        return new MyLog<>(null, "존재하지 않는 메시지입니다");
    }
}
