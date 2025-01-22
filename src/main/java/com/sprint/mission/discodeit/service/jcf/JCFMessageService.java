package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Collection;
import java.util.UUID;


public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    // mocking 이용으로 추가
    private InputHandler inputHandler;
    public JCFMessageService(MessageRepository messageRepository, InputHandler inputHandler){
        this.messageRepository = messageRepository;
        this.inputHandler = inputHandler;
    }
    // mocking 이용으로 추가
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public UUID createMessage(Channel channel, String messageText){
        Message message = new Message(channel, messageText);
        messageRepository.saveMessage(message);
        return message.getId();
    }

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    @Override
    public Collection<Message> showAllMessages(){
        if (messageRepository.findAllMessages().isEmpty()) {
            System.out.println("No Message exists.\n");
            return null;
        }else{
            System.out.println(messageRepository.findAllMessages().toString());
            return messageRepository.findAllMessages();
        }
    }
    @Override
    public Message getMessageById(UUID id){
        return messageRepository.findMessageById(id)
                .orElseGet( () -> {
                    System.out.println(" No  + " + id.toString() + " Message exists.\n");
                    return null;
                });
    }

    // Update : 특정 메세지 수정
    @Override
    public void updateMessageText(UUID id){
        String messageText = inputHandler.getNewInput();
        messageRepository.findMessageById(id).ifPresent( message -> message.setMessageText(messageText));
        messageRepository.findMessageById(id).ifPresent(BaseEntity::refreshUpdateAt);
    }

    // Delete : 전체 메세지 삭제, 특정 메세지 삭제
    @Override
    public void deleteAllMessages(){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            messageRepository.deleteAllMessages();
        }
    }
    @Override
    public void deleteMessageById(UUID id){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            messageRepository.deleteMessageById(id);
        }
    }
}
