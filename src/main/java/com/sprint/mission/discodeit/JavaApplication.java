package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaApplication {
    public static void main(java.lang.String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        /* JCF User Service */
        User userKim = userService.createUser("kim", "mikk@naver.com", "password1");
        User userLee = userService.createUser("lee", "lee2ee@gmail.com", "12341234");
        User userPark = userService.createUser("park", "parkimlee@outlook.com", "parkbarkdark");
        User userOh = userService.createUser("Oh", "ohohoho@gmail.com", "ohohohoohoh");
        User userJung = userService.createUser("Jung", "jungmalbaegopa@naver.com", "jungmalro");

        System.out.println("(userLee) 조회");
        System.out.println(userService.getUserById(userLee.getUserId()) + "\n");

        System.out.println("모든 유저 조회");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println();

        System.out.println("(userPark) 유저 수정");
        userService.updateUser(userPark.getUserId(), "park jin young", "jinyoungpark@gmail.com", "jinyoung123123");
        System.out.println(userService.getUserById(userPark.getUserId()) + "\n");

        System.out.println("(userPark) 삭제");
        userService.deleteUser(userPark.getUserId());
        userService.getAllUsers().forEach(System.out::println);
        System.out.println();



        /* JCF Channel Service */
        System.out.println("채널 등록 및 (code review) 채널 조회");
        Channel channel1 = channelService.createChannel("code review", userKim, new ArrayList<>(List.of(userKim, userLee)));
        Channel channel2 = channelService.createChannel("talking", userOh, new ArrayList<>(List.of(userOh, userLee)));
        System.out.println(channelService.getChannelByName("code review"));

        System.out.println("\n모든 채널 정보 조회");
        channelService.getAllChannels().forEach(System.out::println);

        /**
         * 채널 방장 강퇴 시, 방에 있던 다른 유저가 방장을 넘겨 받음
         */
        System.out.println("\n채널 방장(userOh) 강퇴");
        channelService.kickUserChannel("talking", userOh);
        channelService.getAllChannels().forEach(System.out::println);

        System.out.println("\n일반 유저 강퇴");
        channelService.kickUserChannel("code review", userLee);
        channelService.getAllChannels().forEach(System.out::println);

        System.out.println("\n(code review) 채널 변경, userHan 추가 및 전체 조회");
        channel1.updateChannelName("kakao talk");
        channel1.updateOwnerUser(userLee);
        channel1.updateChannelUsers(Arrays.asList(userOh, userJung));

        User userHan = userService.createUser("Han", "hankook@tire.com", "papapwdwdwd");
        channel1.addUser(userHan);
        channelService.getAllChannels().forEach(System.out::println);

        System.out.println("\n채널 삭제 및 조회");
        channelService.removeChannel("talking");
        channelService.getAllChannels().forEach(System.out::println);



        /* JCF Message Service */
        System.out.println("\n메세지 생성 및 조회");
        Message message1 = messageService.createMessage("안녕", "이건 메일 내용이야", userKim, userLee);
        Message message2 = messageService.createMessage("반갑다", "첫번째 메일 잘 받았어", userLee, userKim);
        System.out.println(messageService.getMessageById(message1.getMessageId()));

        System.out.println("\n메세지 여러건 조회");
        messageService.getAllMessages().forEach(System.out::println);

        System.out.println("\n메세지 수정");
        messageService.updateMessage(message1.getMessageId(), "메세지 제목을 바꿈", "잘 바뀌지?");
        messageService.getAllMessages().forEach(System.out::println);

        System.out.println("\n메세지 삭제");
        messageService.deleteMessage(message1.getMessageId());
        messageService.getAllMessages().forEach(System.out::println);

    }
}
