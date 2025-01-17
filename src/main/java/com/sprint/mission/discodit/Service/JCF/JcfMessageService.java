package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Entity.Message;
import com.sprint.mission.discodit.Service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JcfMessageService implements MessageService {
    private final Map<UUID,Message> map;
    private static volatile JcfMessageService instance;


    public JcfMessageService(Map<UUID, Message> map) {
        this.map = map;
    }
    public static JcfMessageService getInstance() {
        if (instance == null) {
            synchronized (JcfUserService.class) {
                if (instance == null) {
                    instance = new JcfMessageService(new HashMap<>());
                }
            }
        }
        return instance;
    }
    @Override
    public UUID createMessage(UUID sender, String content) { //단순 메시지 생성
        Message message = new Message(sender, content);
        map.put(message.getMessageId(), message);
        return message.getMessageId();
    }
    @Override
    public UUID createMessage(UUID id, UUID sender, String content) { //Channel에서 메시지 생성
        Message message = new Message(sender, content);
        map.put(id, message);
        return message.getMessageId();
    }
    public void initializeMessage(Message message){
        message = new Message();
    }

    @Override
    public Message getMessage(UUID id) {
        Message message = map.get(id);
        return message;
    }
    @Override
    public List<Message> getMessageByUserId(UUID userId) {
        return getMessages().stream().filter(s -> s.getSender().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessages() {
        List<Message> collect = map.entrySet().stream().map(s -> s.getValue()).collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    @Override
    public void setMessage(UUID id, String content) {
        Message message = getMessage(id);
        message.update(content);
        //리턴해서 출력하는 방안 고려
    }

    @Override
    public void deleteMessage(UUID id) {
        if(map.containsKey(id)){
            Message message = map.get(id);
            initializeMessage(message);
            map.remove(id);
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }
    }
}
