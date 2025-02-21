//package com.sprint.mission.discodeit.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class JCFMessageService implements MessageService {
//    private final MessageRepository messageRepository;
//
//    @Override
//    public Message createMessage(User user, Channel channel, String messageContent) {
//        Message newMessage = new Message(user, channel, messageContent);
//        messageRepository.saveMessage(newMessage);
//        System.out.println("\n*** 메시지가 생성되었습니다. ***");
//        System.out.println(newMessage);
//        return newMessage;
//    }
//
//    @Override
//    public void updateMessage(User user, Channel channel, String messageContent, String modifiedMessage) {
//        Message message = messageRepository.findByMessage(messageContent);
//        if (message != null && !message.getMessage().equals(modifiedMessage)) {
//            message.setMessage(modifiedMessage);
//            System.out.println("\n*** 메시지가 변경되었습니다. ***");
//            System.out.println(message);
//        } else {
//            System.out.println("** 메시지를 찾을 수 없거나 변경할 필요가 없습니다. **\n");
//        }
//    }
//
//    @Override
//    public List<Message> findAllMessageList() {
//        System.out.println("\n*** 메시지 전체 조회 ***");
//        List<Message> messages = messageRepository.printAllMessage();
//        messages.forEach(System.out::println);
//        return messages;
//    }
//
//    @Override
//    public void printChannelMessage(User user) {
//        List<Message> userMessages = messageRepository.printByUser(user);
//        if (userMessages.isEmpty()) {
//            System.out.println("** 해당 유저의 메시지가 없습니다. **");
//        } else {
//            System.out.println("\n*** [사용자의 메시지 조회] ***");
//            userMessages.forEach(System.out::println);
//        }
//    }
//
//    @Override
//    public void deleteMessage(String messageContent) {
//        Message messageToDelete = messageRepository.findByMessage(messageContent);
//        if (messageToDelete != null) {
//            messageRepository.deleteMessage(messageToDelete);
//            System.out.println("\n*** 메시지가 삭제되었습니다. ***");
//            System.out.println(messageToDelete);
//        } else {
//            System.out.println("** 메시지를 찾을 수 없습니다. **\n");
//        }
//    }
//}