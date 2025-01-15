package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    //usually don't need direct search for message itself. HashMap -> HashMap + ArrayList -> HashMap + SET
    private List<Message> messageList;
    private JCFChannelService channelService;
    /**
     * deprecated
     * private Map<UUID, List<UUID>> EveryMessageOfUser;
     * private Map<UUID, List<UUID>> ChannelMessage;
     */
    //private final Map<UUID, HashSet<Message>> EveryMessageOfUser; -> deprecated
    //private final Map<UUID, HashSet<Message>> ChannelMessage; -> Channel class's feature

    /**
     * private Map<UUID, HashSet<UUID>> VS Map<UUID, HashSet<Message>>
     * former : In update transaction, only update MessageList. Slow in read transaction...
     * latter : In update transaction, have to update MessageList, EveryMessageOfUser, ChannelMessage
     * Fast in read transaction
     */


    public JCFMessageService() {
        this.messageList = new ArrayList<>();
    }

//        this.EveryMessageOfUser = new HashMap<>(); //syntatic sugar
//        this.ChannelMessage = new HashMap<>();

    public void setChannelService(JCFChannelService channelService) {
        this.channelService = channelService;
    }

    public List<Message> getMesageList() {
        return messageList;
    }

    @Override
    public void createMessage(Message message) {
        messageList.add(message);
        channelService.getChannelList().get(message.getSource()).getChannelMessageList().add(message);
//        EveryMessageOfUser.putIfAbsent(message.getWriter(), new HashSet<>());
//        EveryMessageOfUser.get(message.getWriter()).add(message);
//
//        ChannelMessage.putIfAbsent(message.getSource(), new HashSet<>());
//        ChannelMessage.get(message.getSource()).add(message);
    }
//    @Override
//    public HashSet<Message> readEveryMessageOfUser(UUID userId) {
//        HashSet<Message> msg = EveryMessageOfUser.get(userId);
//        return msg;
//    }






//    @Override
//    public HashSet<Message> readChannelMessage(UUID channelId) {
//        HashSet<Message> msg = ChannelMessage.get(channelId);
//        return msg;
//    }
    @Override
    public void updateMessageById(UUID messageId, String contents) {
        Message msg = messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .get();

        msg.setContents(contents);
        msg.setUpdatedAt();

//        HashSet<Message> umsg = EveryMessageOfUser.get(msg.getWriter());
//        Message user_msg = umsg.stream()
//                .filter(message -> message.getId().equals(messageId))
//                .findFirst()
//                .get();
//
//        user_msg.setContents(contents);
//        user_msg.syncUpdateAt(msg);
//
//        HashSet<Message> cmsg = ChannelMessage.get(msg.getSource());
//        Message channel_msg = cmsg.stream()
//                .filter(message -> message.getId().equals(messageId))
//                .findFirst()
//                .get();
//
//        channel_msg.setContents(contents);
//        channel_msg.syncUpdateAt(msg);

    }
    @Override
    public void deleteMessage(UUID messageId) {
        Message msg = messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .get();

        List<Message> msgList = channelService.readChannelInfo(messageId).getChannelMessageList();

        Message ch_msg = msgList.stream()
                        .filter(message -> message.getId().equals(messageId))
                        .findFirst().orElse(null);

        if (ch_msg != null) {
            msgList.remove(ch_msg);
        }

        messageList.remove(msg);


//        EveryMessageOfUser.remove(msg.getWriter());
//        ChannelMessage.remove(msg.getSource());

    }

    public void printing() {
        messageList.stream()
                .map(msg->msg.toString())
                .forEach(s -> System.out.println(s));
    }
}
