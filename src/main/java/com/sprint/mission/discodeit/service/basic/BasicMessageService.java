package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Collection;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    /**
     * [x ] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
     * => JCF랑 동일하게 작성했습니다
     * [x ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
     * [x ] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
     **/


    MessageRepository messageRepository;
    private InputHandler inputHandler;


    public BasicMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Override
    public UUID createMessage(Channel channel, String messageText) {
        Message message = new Message(channel, messageText);
        messageRepository.saveMessage(message);
        return message.getId();
    }

    @Override
    public Collection<Message> showAllMessages() {
        if (messageRepository.findAllMessages().isEmpty()) {
            System.out.println("No Message exists.\n");
            return null;
        }else{
            System.out.println(messageRepository.findAllMessages().toString());
            return messageRepository.findAllMessages();
        }
    }

    @Override
    public Message getMessageById(UUID id) {
        return messageRepository.findMessageById(id)
                .orElseGet( () -> {
                    System.out.println(" No  + " + id.toString() + " Message exists.\n");
                    return null;
                });
    }

    @Override
    public void updateMessageText(UUID id) {
        String messageText = inputHandler.getNewInput();
        messageRepository.findMessageById(id).ifPresent( message -> message.setMessageText(messageText));
        messageRepository.findMessageById(id).ifPresent(BaseEntity::refreshUpdateAt);
    }

    @Override
    public void deleteAllMessages() {
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            messageRepository.deleteAllMessages();
        }
    }

    @Override
    public void deleteMessageById(UUID id) {
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            messageRepository.deleteMessageById(id);
        }
    }
}
