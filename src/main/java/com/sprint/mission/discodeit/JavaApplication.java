package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


public class JavaApplication {
    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService);

        // 1. 등록
        System.out.println("1. 등록");
        // 1.1 유저 등록
        System.out.println("1.1 유저 등록(user1, user2):");
        User user1 = new User("user1", "user1@example.com", "01012345678");
        try{
            userService.create(user1);
            System.out.println(user1.getUsername() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        User user2 = new User("user2", "user2@example.com", "01087654321");
        try {
            userService.create(user2);
            System.out.println(user2.getUsername() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 1.1.1 중복 유저 이름 등록
        System.out.println("\n1.1.1 중복 유저 이름 등록:");
        User usernameDuple = new User("user1", "user3@example.com", "01087654321");
        try {
            userService.create(usernameDuple);
            System.out.println(usernameDuple.getUsername() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 1.1.2 유효하지 않은 이메일 등록
        System.out.println("\n1.1.2 유효하지 않은 이메일 생성:");
        User emailInvalid = new User("emailInvalid", "email@examplecom", "01087654321");
        try {
            userService.create(emailInvalid);
            System.out.println(emailInvalid.getEmail() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 1.2 채널 등록
        System.out.println("\n1.2 채널 생성:");
        Channel channel1 = new Channel("Sprint");
        try{
            channelService.create(channel1);
            System.out.println(channel1.getName() + " 생성 성공");
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        Channel channel2 = new Channel("codeit");
        try{
            channelService.create(channel2);
            System.out.println(channel2.getName() + " 생성 성공");
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        // 1.2.1 유효하지 않은 채널 등록
        System.out.println("\n1.2.1 유효하지 않은 채널 등록:");
        Channel channelInvalid = new Channel("");
        try{
            channelService.create(channelInvalid);
            System.out.println(channelInvalid.getName() + " 생성 성공");
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        // 1.3 메시지 등록
        System.out.println("\n1.3 메시지 등록:");
        Message message1 = new Message("Hello world!", user1);
        try {
            messageService.create(message1);
            System.out.println(message1.getContent() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        Message message2 = new Message("Hello java!", user2);
        try {
            messageService.create(message2);
            System.out.println(message2.getContent() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 1.3.1 유효하지 않은 메시지 등록
        System.out.println("\n1.3.1 유효하지 않은 메시지 등록:");
        Message messageInvalid = new Message("", user1);
        try {
            messageService.create(messageInvalid);
            System.out.println(messageInvalid.getContent() + " 생성 성공");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 1.3.2 유효하지 않은 유저의 메시지 등록:
        System.out.println("\n1.3.2 유효하지 않은 유저의 메시지 등록:");
        User errorUser = new User("errorUser", "error@example.com", "01011111111");
        Message createMessage = new Message("Invalid message content", errorUser);
        try {
            Message invalidMessage = messageService.create(createMessage);
            System.out.println(messageInvalid.getContent() + " 생성 성공");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 2. 조회 (단건, 다건)
        System.out.println("\n2. 조회:");
        System.out.println("2.1 유저 조회:");
        System.out.println("2.1.1 아이디로 유저 조회");
        User searchUser = userService.readById(user1.getId());
        System.out.println("찾고자하는 유저: " + user1.getUsername() + ", 조회한 유저: " + searchUser.getUsername());

        System.out.println("\n2.1.2 전체 유저 조회");
        userService.readAll().stream()
                .map(User::getUsername)
                .sorted()
                .forEach(username -> System.out.print(username + " "));
        System.out.println();

        System.out.println("\n2.2 채널 조회:");
        System.out.println("2.2.1 아이디로 채널 조회");
        Channel searchChannel = channelService.readById(channel1.getId());
        System.out.println("찾고자하는 채널: " + channel1.getName() + ", 조회한 채널: " + searchChannel.getName());

        System.out.println("\n2.2.2 전체 채널 조회");
        channelService.readAll().stream()
                .map(Channel::getName)
                .sorted()
                .forEach(channel -> System.out.print(channel + " "));
        System.out.println();

        System.out.println("\n2.3 메시지 조회:");
        System.out.println("2.3.1 아이디로 메시지 조회");
        Message searchMessage = messageService.readById(message1.getId());
        System.out.println("찾고자하는 채널: " + message1.getContent() + ", 조회한 채널: " + searchMessage.getContent());

        System.out.println("\n2.3.2 전체 메시지 조회");
        messageService.readAll().stream()
                .map(Message::getContent)
                .sorted()
                .forEach(message -> System.out.print(message + " "));
        System.out.println();

        // 3. 수정 및 데이터 조회
        System.out.println("\n3. 수정 및 데이터 조회:");
        user2.update("user2_update", "user2_update", "user2_update@example.com", "99999999");
        channel2.update("codeit_update");
        message2.update("Hello java! update");

        User updatedUser = userService.readById(user2.getId());
        System.out.println("수정된 유저 조회: " + updatedUser.getUsername());

        Channel updatedChannel = channelService.readById(channel2.getId());
        System.out.println("수정된 채널 조회: " + updatedChannel.getName());

        Message updatedMessage = messageService.readById(message2.getId());
        System.out.println("수정된 메시지 조회: " + updatedMessage.getContent());

        // 4. 삭제 및 데이터 조회
        System.out.println("\n4. 삭제 및 데이터 조회:");
        userService.delete(user2.getId());
        channelService.delete(channel2.getId());
        messageService.delete(message2.getId());

        System.out.print("모든 유저: ");
        userService.readAll().stream()
                .map(User::getUsername)
                .forEach(username -> System.out.print(username + " "));
        System.out.println();

        System.out.print("모든 채널: ");
        channelService.readAll().stream()
                .map(Channel::getName)
                .forEach(channel -> System.out.print(channel + " "));
        System.out.println();

        System.out.println("모든 메시지: ");
        messageService.readAll().stream()
                .map(Message::getContent)
                .forEach(System.out::println);
    }
}