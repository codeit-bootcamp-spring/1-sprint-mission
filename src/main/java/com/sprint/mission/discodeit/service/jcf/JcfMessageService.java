package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JcfMessageService implements MessageService {
    //private final Map<UUID,Message> map;
    private static volatile JcfMessageService instance;
    private final JCFMessageRepository jcfMessageRepository;

    public JcfMessageService(JCFMessageRepository jcfMessageRepository) {
        this.jcfMessageRepository = jcfMessageRepository;
    }

    public static JcfMessageService getInstance() {
        if (instance == null) {
            synchronized (JcfUserService.class) {
                if (instance == null) {
                    instance = new JcfMessageService(new JCFMessageRepository());//messageMap
                }
            }
        }
        return instance;
    }
    @Override
    public UUID createMessage(UUID sender, String content) { //단순 메시지 생성
        return jcfMessageRepository.save(sender, content);
    }

    @Override
    public Message getMessage(UUID id) {
        //Message message = map.get(id);
        return jcfMessageRepository.findMessageById(id);
    }
    @Override
    public List<Message> getMessagesByUserId(UUID userId) {
        return jcfMessageRepository.findMessagesById(userId);
    }

    @Override
    public List<Message> getMessages() {

        return jcfMessageRepository.getMessages();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        jcfMessageRepository.update(id, content);
        //리턴해서 출력하는 방안 고려
    }

    @Override
    public void deleteMessage(UUID id) {
        jcfMessageRepository.delete(id);
    }
}
