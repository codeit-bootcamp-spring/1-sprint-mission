package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.Optional;

public class JavaApplication {
    static ChannelService channelService = JCFChannelService.getInstance();
    static UserService userService = JCFUserService.getInstance();
    static MessageService messageService = JCFMessageService.getInstance();
    public static void main(String[] args) {
        userSimulation();
        channelSimulation();
        messageSimulation();
    }

    static void userSimulation(){
        // 유저 생성
        User user = new User(
            "exampleUsername",
            "examplePwd",
            "example@gmail.com",
            "exampleNickname",
            "01012345678",
            "https:://example-url.com",
            "this is example description"
        );

        User user2 = new User(
            "exampleUsername2",
            "examplePwd2",
            "example2@gmail.com",
            "exampleNickname2",
            "01013345678",
            "https:://example-url.com2",
            "this is example description2"
        );

        userService.createUser(user);
        userService.createUser(user2);

        // 유저 조회
        String userId = user.getUUID();
        User fetchedUser = userService.readUserById(userId).get();

        System.out.println(fetchedUser);

        // 여러 유저 조회
        List<User> users = userService.readAllUsers();
        System.out.println("=== 여러 유저 조회 ===");
        for(User u : users){
            System.out.println(u);
        }

        // 유저 정보 업데이트

        UserUpdateDto updateDto = new UserUpdateDto(
            Optional.of("changedUsername"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );

        userService.updateUser(userId, updateDto, "examplePwd");

        // 업데이트 유저 출력, 동등성
        User updatedUser = userService.readUserById(userId).get();
        System.out.println(fetchedUser);
        System.out.println(fetchedUser == updatedUser);

        // 비밀번호 틀릴시 업데이트 x
        try {
            userService.updateUser(userId, updateDto, "1");
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 유저 삭제 비밀번호 틀립
        try {
            userService.deleteUser(userId, "examplePwdㅁ");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 유저 삭제 비밀번호 동일
        try {
            userService.deleteUser(userId, "examplePwd");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        System.out.println(userService.readUserById(userId).isEmpty());
    }

    static void channelSimulation(){

        // 채널 생성
        Channel chatChannel = new Channel(
            "server-uuid-1",
            "category-uuid-1",
            "exampleChannelName",
            100,
            false,
            "General",
            new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
        );

        Channel voiceChannel = new Channel(
            "server-uuid-1",
            "category-uuid-1",
            "exampleChannelName",
            100,
            false,
            "General",
            new VoiceBehavior()
        );

        String channelId = chatChannel.getUUID();
        channelService.createChannel(chatChannel);
        channelService.createChannel(voiceChannel);
        Channel fetchedChannel = channelService.getChannelById(channelId).get();
        System.out.println(fetchedChannel);

        System.out.println("=== 여러 채널 조회 ===");

        List<Channel> channels = channelService.getAllChannels();

        for(Channel c : channels){
            System.out.println(c);
        }

        // 체널 수정
        ChannelUpdateDto updateDto = new ChannelUpdateDto(
            Optional.of("updatedChannelName"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );

        channelService.updateChannel(channelId, updateDto);

        System.out.println(channelService.getChannelById(channelId).get());

        // 채팅 채널

        User user = userService.readAllUsers().get(0);
        String userId = user.getUUID();
        System.out.println(user);

        ChatBehavior chat = (ChatBehavior) chatChannel.getBehavior();

        chat.addMessage( new Message(
            userId,
            chatChannel.getUUID(),
            "this is example message",
            null,
            null
        ));

        chat.addMessage( new Message(
            userId,
            chatChannel.getUUID(),
            "this is example message2",
            null,
            null
        ));

        chat.addMessage( new Message(
            userId,
            chatChannel.getUUID(),
            "this is final message",
            null,
            null
        ));

        // 메시지 내역 출력
        System.out.println("=== 채팅 채널 메시지 내역 ===");
        List<Message> history = chat.getChatHistory();
        for(Message m : history){
            System.out.println(m);
        }

        // 존재하지 않는 user 의 메시지는 추가할 수 없음

        try {
            chat.addMessage(new Message(
                "randomUserID",
                chatChannel.getUUID(),
                "this error test",
                null,
                null
            ));
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        // 체널 삭제

        channelService.deleteChannel(channelId);
        System.out.println(channelService.getAllChannels().size() == 1);

    }

    static void messageSimulation(){
        User user = userService.readAllUsers().get(0);
        Channel chatChannel = new Channel(
            "server-uuid-2",
            "category-uuid-2",
            "exampleChannelName",
            100,
            false,
            "General",
            new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
        );

        Channel chatChannel2 = new Channel(
            "server-uuid-1",
            "category-uuid-1",
            "exampleChannelName2",
            100,
            false,
            "General",
            new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
        );



        ChatBehavior chatBehavior = (ChatBehavior) chatChannel.getBehavior();
        ChatBehavior chatBehavior2 = (ChatBehavior) chatChannel2.getBehavior();

        channelService.createChannel(chatChannel);
        channelService.createChannel(chatChannel2);
        Message message = new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is first chat!",
            null,
            null
        );

        messageService.createMessage(message, chatBehavior);

        messageService.createMessage(new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is example message",
            null,
            null
        ), chatBehavior2 );


        messageService.createMessage(new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is example message2",
            null,
            null
        ), chatBehavior2 );


        messageService.createMessage(new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is example message3",
            null,
            null
        ), chatBehavior2 );

        List<Channel> channels = channelService.getAllChannels();

        // 채널별 메시지 조회
        for(Channel c : channels){
            if(c.getBehavior() instanceof ChatBehavior){
                ChatBehavior currentChat = (ChatBehavior) c.getBehavior();
                List<Message> currentChatHistory = messageService.getChatHistory(currentChat);
                System.out.println(c.getChannelName() + " Chat History");
                for(Message m : currentChatHistory){
                    System.out.println(m.getContent());
                }
            }
        }

        // channel1 메시지 조회
        System.out.println("=== channel 1 message history === ");
        for(Message m : messageService.getChatHistory(chatBehavior)){
            System.out.println(m);
        }

        // channel1 메시지 수정

        System.out.println("=== edit message ===");
        String messageId = messageService.getChatHistory(chatBehavior).get(0).getUUID();
        messageService.modifyMessage(messageId, "this is edited message", chatBehavior);

        System.out.println(messageService.getMessageById(messageId, chatBehavior).get().getContent());
        // channel1 메시지 삭제


        messageService.deleteMessage(messageId, chatBehavior);
        //삭제 후 조회

        System.out.println("=== channel 1 message history === ");
        for(Message m : messageService.getChatHistory(chatBehavior)){
            System.out.println(m);
        }

    }
}
