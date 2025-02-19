//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.view.ConsoleView;
//
//import java.util.*;
//
//public class JCFMessageService implements MessageService {
//    private final List<Message> messages = new ArrayList<>();
//    private final ChannelService channelService;
//    private final UserService userService;
//    private final ConsoleView view;
//
//    public JCFMessageService(ChannelService channelService, UserService userService, ConsoleView view) {
//        this.channelService = channelService;
//        this.userService = userService;
//        this.view = view;
//    }
//
//    @Override
//    public Message createMessage(UUID userId, UUID channelId, String content) {
//        Channel channel = channelService.getChannelById(channelId);
//        if (channel == null) {
//            view.displayError("Channel을 찾을 수 없습니다!");
//            return null;
//        }
//
//        User user = userService.getUserById(userId);
//        if (user == null) {
//            view.displayError("유저를 찾을 수 없습니다");
//            return null;
//        }
//
//        Message message = new Message(userId, channelId, content);
//        messages.add(message);
//        view.displaySuccess("메시지 생성 성공!");
//        return message;
//    }
//
//    @Override
//    public Message getMessageById(UUID messageId) {
//        for (Message message : messages) {
//            if (message.getId().equals(messageId)) {
//                return message;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Message> getMessagesByChannel(UUID channelId) {
//        List<Message> channelMessages = new ArrayList<>();
//        for (Message message : messages) {
//            if (message.getChannelId().equals(channelId)) {
//                channelMessages.add(message);
//            }
//        }
//        return channelMessages;
//    }
//    @Override
//    public List<Message> getMessagesBySender(UUID senderId) {
//        List<Message> senderMessages = new ArrayList<>();
//        for (Message message : messages) {
//            if (message.getSenderId().equals(senderId)) {
//                senderMessages.add(message);
//            }
//        }
//        return senderMessages;
//    }
//
//
//    @Override
//    public List<Message> getAllMessages() {
//        return new ArrayList<>(messages);
//    }
//
//
//    @Override
//    public Message updateMessageContent(UUID messageId, String newContent) {
//        for (Message message : messages) {
//            if (message.getId().equals(messageId)) {
//                message.updateContent(newContent);
//                return message;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public boolean deleteMessage(UUID messageId) {
//        for (int i = 0; i < messages.size(); i++) {
//            if (messages.get(i).getId().equals(messageId)) {
//                messages.remove(i);
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
