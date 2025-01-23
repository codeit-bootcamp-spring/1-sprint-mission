package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private List<Message> messageList;

    private JCFMessageService() {
        this.messageList = new ArrayList<>();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }


    public List<Message> getMesageList() {
        return messageList;
    }

    @Override
    public void createMessage(Message message) {
        messageList.add(message);
        JCFChannelService.getInstance().getChannelList().get(message.getSource()).getChannelMessageList().add(message);
    }



    @Override
    public void updateMessageById(UUID messageId, String contents) {
        Message messageForUpdate = messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .get();
        messageForUpdate.setContents(contents);
        messageForUpdate.setUpdatedAt();
    }
    @Override //delete message -> related class : JCFMessage, channel
    public void deleteMessage(UUID messageId) {
        //messageList.removeIf(msg -> msg.getId().equals(messageId));

        Message messageForDelete = messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .get();



        List<Message> mesgList = JCFChannelService.getInstance().getChannelList().get(messageForDelete.getSource()).getChannelMessageList();

        //traverse in range of msgList is better than messageList...
        Message ch_msg = mesgList.stream()
                        .filter(message -> message.getId().equals(messageId))
                        .findFirst().orElse(null);

        if (ch_msg != null) {
            mesgList.remove(ch_msg);
        }

        messageList.remove(messageForDelete);
    }

    public void printing() {
        messageList.stream()
                .map(msg->msg.toString())
                .forEach(s -> System.out.println(s));
    }
}
