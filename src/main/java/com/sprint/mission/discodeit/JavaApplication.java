package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageServiceV2;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.Optional;

public class JavaApplication {
    static ChannelService channelService = JCFChannelService.getInstance();
    static UserService userService = JCFUserService.getInstance();
    static MessageService messageService = JCFMessageService.getInstance();
    static MessageServiceV2 messageServiceV2 = JCFMessageServiceV2.getInstance();
    public static void main(String[] args) {
//        userSimulation();
//        channelSimulation();
//        messageSimulation();
        userSimulation();
        channelSimulation2();
        messageSimulation2();
    }

    static void userSimulation(){
        // 유저 생성

        System.out.println("=== 유저 생성 및 조회 ===");
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

        System.out.println("\n=== 여러 유저 조회 ===");
        for(User u : users){
            System.out.println(u);
        }

        // 유저 정보 업데이트
        System.out.println("\n=== 유저 정보 업데이트 ===");
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

        System.out.println("\n=== 유저 데이터 조작 비밀번호 오류 ===");
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

        System.out.println("\n=== 삭제 후 유저 x ===");
        // 유저 삭제 비밀번호 동일
        try {
            userService.deleteUser(userId, "examplePwd");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        System.out.println(userService.readUserById(userId).isEmpty());
    }

    static void channelSimulation2(){

        System.out.println("\n=== 채널 생성 / 조회 ===");

        Channel chatChannel = new Channel(
            "server-uuid-1",
            "category-uuid-1",
            "exampleChatName",
            100,
            false,
            "General",
            new ChatBehaviorV2(userService, messageServiceV2)
        );

        Channel voiceChannel = new Channel(
            "server-uuid-1",
            "category-uuid-1",
            "exampleVoiceName",
            100,
            false,
            "General",
            new VoiceBehavior()
        );

        String chatChannelId = chatChannel.getUUID();
        String voiceChannelId = voiceChannel.getUUID();

        channelService.createChannel(chatChannel);
        channelService.createChannel(voiceChannel);
        System.out.println(channelService.getChannelById(chatChannelId).get());

        System.out.println("\n=== 모든 채널 조회 ===");
        List<Channel> channels = channelService.getAllChannels();
        channels.stream().forEach(System.out::println);

        System.out.println("\n=== 채널 업데이트 ===");
        ChannelUpdateDto updatedChannel = new ChannelUpdateDto(
            Optional.of("updatedChannelName"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );

        channelService.updateChannel(chatChannelId, updatedChannel);
        System.out.println(channelService.getChannelById(chatChannelId).get());

        System.out.println("\n=== 음성 채널 삭제 후 전채 조회 ===");
        channelService.deleteChannel(voiceChannelId);

        List<Channel> channels2 = channelService.getAllChannels();
        channels2.stream().forEach(System.out::println);

    }

    static void messageSimulation2(){

        System.out.println("\n=== 메시지를 보낼 유저와 채널 생성 ===");
        User user = new User(
            "exampleUsername",
            "examplePwd",
            "example@gmail.com",
            "exampleNickname",
            "01012345678",
            "https:://example-url.com",
            "this is example description"
        );
        userService.createUser(user);

        Channel chatChannel = new Channel(
            "server-uuid-2",
            "category-uuid-2",
            "exampleChannelName",
            100,
            false,
            "General",
            new ChatBehaviorV2(userService, messageServiceV2)
        );

        Channel chatChannel2 = new Channel(
            "server-uuid-1",
            "category-uuid-1",
            "exampleChannelName2",
            100,
            false,
            "General",
            new ChatBehaviorV2(userService, messageServiceV2)
        );

        ChatBehaviorV2 chatBehavior = (ChatBehaviorV2) chatChannel.getBehavior();
        ChatBehaviorV2 chatBehavior2 = (ChatBehaviorV2) chatChannel2.getBehavior();

        channelService.createChannel(chatChannel);
        channelService.createChannel(chatChannel2);

        Message message = new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is first Chat",
            null,
            null
        );

        chatBehavior.setChannel(chatChannel);
        chatBehavior2.setChannel(chatChannel2);

        chatBehavior.addMessage(message);
        chatBehavior.addMessage(new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is second Chat",
            null,
            null
        ));

        chatBehavior.addMessage(new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is third Chat",
            null,
            null
        ));

        chatBehavior2.addMessage(new Message(
            user.getUUID(),
            chatChannel.getUUID(),
            "this is second channel first Chat",
            null,
            null
        ));

        Message m = chatBehavior.getSingleMessage(message.getUUID());
        System.out.println(m);

        System.out.println("\n=== 1번 채널 채팅 내역 ===");
        chatBehavior.getChatHistory().stream().forEach(System.out::println);

        System.out.println("\n=== 2번 채널 채팅 내역 ===");
        chatBehavior2.getChatHistory().stream().forEach(System.out::println);

        System.out.println("\n=== 메시지 업데이트 ===");
        MessageUpdateDto updateMessage = new MessageUpdateDto(Optional.of("this is updated content"), Optional.empty());
        chatBehavior.updateMessage(message.getUUID() ,updateMessage);
        System.out.println(chatBehavior.getSingleMessage(message.getUUID()));

        System.out.println("\n=== 1번 채널 채팅 삭제 ===");
        chatBehavior.deleteMessage(message.getUUID());
        chatBehavior.getChatHistory().stream().forEach(System.out::println);

        System.out.println("\n=== 존재하지 않는 유저의 메시지 생성 ===");
        Message falseUserMessage = new Message(
            "false-user-uuid",
            chatChannel.getUUID(),
            "false",
            null,
            null
        );
        try {
            chatBehavior.addMessage(falseUserMessage);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
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
