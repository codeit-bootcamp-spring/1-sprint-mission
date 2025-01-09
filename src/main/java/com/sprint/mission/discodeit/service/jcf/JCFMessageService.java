package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChatBehavior;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService messageRepository;
    // private final Map<String, Message> data;

    private JCFMessageService(){
        // data = new HashMap<>();
    }
    public static JCFMessageService getInstance(){
        if(messageRepository == null){
            synchronized (JCFMessageService.class){
                if(messageRepository == null){
                    messageRepository = new JCFMessageService();
                }
            }
        }
        return messageRepository;
    }

    @Override
    public Message createMessage(Message message, ChatBehavior chatBehavior) {
       // data.put(message.getUUID(), message);
        chatBehavior.addMessage(message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(String messageUUID, ChatBehavior chatBehavior) {
//        return Optional.ofNullable(data.get(messageUUID));
        return Optional.ofNullable(chatBehavior.getMessageById(messageUUID));
    }

    @Override
    public List<Message> getMessagesByChannel(String channelUUID) {
        return null;
    }

    @Override
    public List<Message> getMessagesByThread(String threadUUID) {
        return null;
    }

    @Override
    public Message updateMessage(String messageUUID, String newContent, String newContentImageUrl, ChatBehavior chatBehavior) {

        Message message = chatBehavior.getMessageById(messageUUID);

        message.setContent(newContent);
        message.setContentImage(newContentImageUrl);
        message.setIsEdited();

        return null;
    }

    @Override
    public boolean deleteMessage(String messageUUID, ChatBehavior chatBehavior) {
        chatBehavior.deleteMessage(messageUUID);
        return true;
    }

    @Override
    public List<Message> getChatHistory(ChatBehavior chatBehavior){
        return Collections.unmodifiableList(new ArrayList<>(chatBehavior.getChatHistory()));
    }

    // TODO : ChatBehavoir 연결
    @Override
    public void addReactionToMessage(String messageUUID, String userUUID, String reactionType) {

    }

    // TODO : ChatBehavoir 연결
    @Override
    public void removeReactionFromMessage(String messageUUID, String userUUID) {

    }


    public void modifyMessage(String messageId, String content, ChatBehavior chatBehavior){
        chatBehavior.editMessage(messageId, content);
    }

}
