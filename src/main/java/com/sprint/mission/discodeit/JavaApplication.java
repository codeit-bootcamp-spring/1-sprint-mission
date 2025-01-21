package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.AppConfig;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static void main(java.lang.String[] args) {

        EntityFactory entityFactory = BaseEntityFactory.getInstance();
        AppConfig config = new AppConfig();
        UserService userService = config.basicUserService();
        ChannelService channelService = config.basicChannelService();

        User lee = userService.createUser("lee", "lee@gmail.com", "leeeelee");
        User kim = userService.createUser("kim", "kim@gmail.com", "kimkimkikim");
        User park = userService.createUser("park", "park@gmail.com", "parkapkark");
        Map<UUID, User> allUsers = userService.getAllUsers();

        channelService.createChannel("ch.1", lee, allUsers);

        Map<UUID, Channel> allChannels = channelService.getAllChannels();

        System.out.println(allChannels);

        userService.updateUser(lee.getUserId(), "newNameLee", "newLee@gmail.com", "chanmgePassword");

        System.out.println(allChannels);


//        // FileUserService
//        UserService userService = new FileUserService(entityFactory);
//        ChannelService channelService = new FileChannelService(entityFactory);
//        MessageService messageService = new FileMessageService(entityFactory);
//
//        System.out.println("유저 추가 및 출력");
//        User userKim = userService.createUser("kim", "mikk@naver.com", "password1");
//        User userLee = userService.createUser("lee", "lee2ee@gmail.com", "12341234");
//        User userPark = userService.createUser("park", "parkimlee@outlook.com", "parkbarkdark");
//        User userOh = userService.createUser("Oh", "ohohoho@gmail.com", "ohohohoohoh");
//        User userJung = userService.createUser("Jung", "jungmalbaegopa@naver.com", "jungmalro");
//        userService.getAllUsers().entrySet().forEach(System.out::println);
//
//        System.out.println("\n[userKim] 유저 수정");
//        userService.updateUser(userKim.getUserId(), "gim", "ilovegim@gmail.com", "gimgimgim");
//        userService.getAllUsers().entrySet().forEach(System.out::println);
//
//        System.out.println("\n[userLee] 유저 삭제");
//        userService.deleteUser(userLee.getUserId());
//        userService.getAllUsers().entrySet().forEach(System.out::println);
//
//        System.out.println();
//
//        Map<UUID, User> userMap1 = new HashMap<>();
//        userMap1.put(userKim.getUserId(), userKim);
//        userMap1.put(userLee.getUserId(), userLee);
//
//        Map<UUID, User> userMap2 = new HashMap<>();
//        userMap2.put(userOh.getUserId(), userOh);
//        userMap2.put(userLee.getUserId(), userLee);
//
//        System.out.println("채널 등록 및 (code review) 채널 조회");
//        Channel channel1 = channelService.createChannel("code review", userKim, userMap1); //kim lee
//        Channel channel2 = channelService.createChannel("talking", userOh, userMap2);
//        System.out.println(channelService.getChannelByName("code review"));
//
//        System.out.println("\n모든 채널 정보 조회");
//        channelService.getAllChannels().entrySet().forEach(System.out::println);
//
//        System.out.println("\n채널 방장(userOh) 강퇴");
//        channelService.kickUserChannel(channel2.getChannelId(), userOh);
//        channelService.getAllChannels().entrySet().forEach(System.out::println);
//
//        System.out.println("\n일반 유저 강퇴");
//        channelService.kickUserChannel(channel1.getChannelId(), userLee);
//        channelService.getAllChannels().entrySet().forEach(System.out::println);
//
//        System.out.println("\n(code review) 채널 변경");
//        channelService.updateChannel(channel1.getChannelId(), "kakao Talk", userLee);
//        channelService.getAllChannels().entrySet().forEach(System.out::println);
//
//
//        System.out.println("\nuserHan 추가 및 전체 조회");
//        User userHan = userService.createUser("Han", "hankook@tire.com", "papapwdwdwd");
//        channelService.addUserChannel(channel1.getChannelId(), userHan);
//        channelService.getAllChannels().entrySet().forEach(System.out::println);
//
//        System.out.println("\n채널 삭제 및 조회");
//        channelService.removeChannelById(channel2.getChannelId());
//        channelService.getAllChannels().entrySet().forEach(System.out::println);
//
//        System.out.println();
//
//
//        System.out.println("\n메세지 생성 및 조회");
//        Message message1 = messageService.createMessage("안녕", "이건 메일 내용이야", userKim, userLee);
//        Message message2 = messageService.createMessage("반갑다", "첫번째 메일 잘 받았어", userLee, userKim);
//        System.out.println(messageService.getMessageById(message1.getMessageId()));
//
//        System.out.println("\n메세지 여러건 조회");
//        messageService.getAllMessages().entrySet().forEach(System.out::println);
//
//        System.out.println("\n메세지 수정");
//        messageService.updateMessage(message1.getMessageId(), "메세지 제목을 바꿈", "잘 바뀌지?");
//        messageService.getAllMessages().entrySet().forEach(System.out::println);
//
//        System.out.println("\n메세지 삭제");
//        messageService.deleteMessage(message1.getMessageId());
//        messageService.getAllMessages().entrySet().forEach(System.out::println);



/*
**      JCF 구현 테스트
*
*
        EntityFactory entityFactory = BaseEntityFactory.getInstance();

        UserService userService = new JCFUserService(entityFactory);
        ChannelService channelService = new JCFChannelService(entityFactory);
        MessageService messageService = new JCFMessageService(entityFactory);

        *//* JCF User Service *//*
        User userKim = userService.createUser("kim", "mikk@naver.com", "password1");
        User userLee = userService.createUser("lee", "lee2ee@gmail.com", "12341234");
        User userPark = userService.createUser("park", "parkimlee@outlook.com", "parkbarkdark");
        User userOh = userService.createUser("Oh", "ohohoho@gmail.com", "ohohohoohoh");
        User userJung = userService.createUser("Jung", "jungmalbaegopa@naver.com", "jungmalro");

        System.out.println("(userLee) 조회");
        System.out.println(userService.getUserById(userLee.getUserId()) + "\n");

        System.out.println("모든 유저 조회");
        userService.getAllUsers().entrySet().forEach(System.out::println);
        System.out.println();

        System.out.println("(userPark) 유저 수정");
        userService.updateUser(userPark.getUserId(), "park jin young", "jinyoungpark@gmail.com", "jinyoung123123");
        System.out.println(userService.getUserById(userPark.getUserId()) + "\n");

        System.out.println("(userPark) 삭제");
        userService.deleteUser(userPark.getUserId());
        userService.getAllUsers().entrySet().forEach(System.out::println);
        System.out.println();



        *//* JCF Channel Service *//*
        Map<UUID, User> userMap1 = new HashMap<>();
        userMap1.put(userKim.getUserId(), userKim);
        userMap1.put(userLee.getUserId(), userLee);

        Map<UUID, User> userMap2 = new HashMap<>();
        userMap2.put(userOh.getUserId(), userOh);
        userMap2.put(userLee.getUserId(), userLee);

        System.out.println("채널 등록 및 (code review) 채널 조회");
        Channel channel1 = channelService.createChannel("code review", userKim, userMap1); //kim lee
        Channel channel2 = channelService.createChannel("talking", userOh, userMap2);
        System.out.println(channelService.getChannelByName("code review"));

        System.out.println("\n모든 채널 정보 조회");
        channelService.getAllChannels().entrySet().forEach(System.out::println);

        *//**
         * 채널 방장 강퇴 시, 방에 있던 다른 유저가 방장을 넘겨 받음
         *//*
        System.out.println("\n채널 방장(userOh) 강퇴");
        channelService.kickUserChannel(channel2.getChannelId(), userOh);
        channelService.getAllChannels().entrySet().forEach(System.out::println);

        System.out.println("\n일반 유저 강퇴");
        channelService.kickUserChannel(channel1.getChannelId(), userLee);
        channelService.getAllChannels().entrySet().forEach(System.out::println);

        System.out.println("\n(code review) 채널 변경");
        channel1.updateChannelName("kakao talk");
        channel1.updateOwnerUser(userLee);
        channel1.updateChannelUsers(Map.of(userOh.getUserId(), userOh, userJung.getUserId(), userJung));

        System.out.println("\nuserHan 추가 및 전체 조회");
        User userHan = userService.createUser("Han", "hankook@tire.com", "papapwdwdwd");
        channel1.addUser(userHan);
        channelService.getAllChannels().entrySet().forEach(System.out::println);

        System.out.println("\n채널 삭제 및 조회");
        channelService.removeChannelById(channel2.getChannelId());
        channelService.getAllChannels().entrySet().forEach(System.out::println);

        System.out.println("\n채널 업데이트");
        channelService.updateChannel(channel1.getChannelId(), "바뀐채널", userHan);
        channelService.getAllChannels().entrySet().forEach(System.out::println);



        *//* JCF Message Service *//*
        System.out.println("\n메세지 생성 및 조회");
        Message message1 = messageService.createMessage("안녕", "이건 메일 내용이야", userKim, userLee);
        Message message2 = messageService.createMessage("반갑다", "첫번째 메일 잘 받았어", userLee, userKim);
        System.out.println(messageService.getMessageById(message1.getMessageId()));

        System.out.println("\n메세지 여러건 조회");
        messageService.getAllMessages().entrySet().forEach(System.out::println);

        System.out.println("\n메세지 수정");
        messageService.updateMessage(message1.getMessageId(), "메세지 제목을 바꿈", "잘 바뀌지?");
        messageService.getAllMessages().entrySet().forEach(System.out::println);

        System.out.println("\n메세지 삭제");
        messageService.deleteMessage(message1.getMessageId());
        messageService.getAllMessages().entrySet().forEach(System.out::println);*/

    }
}
