package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static void main(java.lang.String[] args) {
        EntityFactory entityFactory = BaseEntityFactory.getInstance();

        // 지금 AppConfig는 FileIO를 구현한 클래스들로 의존성 주입하고 있음
        AppConfig config = new AppConfig();
        UserService userService = config.basicUserService();
        ChannelService channelService = config.basicChannelService();
        MessageService messageService = config.basicMessageService();

        /**
         * @Description: FileIO와 객체 직렬화를 이용한 **유저** 서비스 테스트
         */
        FileUserService userFileService = new FileUserService();
        User userKim = userFileService.createUser("kim", "kim@gmail.com", "passwordkim");
        User userLee = userFileService.createUser("lee", "lee@gmail.com", "passwordlee");
        User userJung = userFileService.createUser("jung", "jung@gmail.com", "passwordjung");
        User userPark = userFileService.createUser("park", "park@gmail.com", "passwordpark");


        System.out.println("유저 추가 및 userKim id로 조회");
        User findUser = userFileService.getUserById(userKim.getUserId());
        System.out.println(findUser);

        System.out.println("\n모든 유저 조회");
        Map<UUID, User> allUsers = userFileService.getAllUsers();
        System.out.println(allUsers);

        System.out.println("\nuserKim 유저 수정 -> name='gim'");
        userFileService.getUserById(userKim.getUserId()).updateName("gim");
        System.out.println(userFileService.getUserById(userKim.getUserId()));

        System.out.println("\nuserLee 유저 삭제");
        userFileService.deleteUser(userLee.getUserId());
        System.out.println(userFileService.getAllUsers());


        /**
         * @Description: FileIO와 객체 직렬화를 이용한 **채널** 서비스 테스트
         */
        FileChannelService channelFileService = new FileChannelService();
        Map<UUID, User> userMap1 = new HashMap<>();
        userMap1.put(userKim.getUserId(), userKim);
        userMap1.put(userLee.getUserId(), userLee);

        System.out.println("\n채널 등록 및 (code review) 채널 조회");
        Channel codeReviewChannel = channelFileService.createChannel("code review", userKim, userMap1);
        System.out.println(channelFileService.getChannelByName("code review"));

        System.out.println("\n채널 이름으로 조회");
        System.out.println(channelFileService.getChannelByName("code review"));

        System.out.println("\n모든 채널 정보 조회");
        Channel talkingChannel = channelFileService.createChannel("talking", userLee, new HashMap<>());
        System.out.println(channelFileService.getAllChannels());

        System.out.println("\n(code review) 채널 변경");
        channelFileService.updateChannel(codeReviewChannel.getChannelId(), "play game with me", userLee);
        System.out.println(channelFileService.getAllChannels());

        System.out.println("\nuserHan 추가 및 전체 조회");
        User userHan = userFileService.createUser("Han", "han@gmail.com", "passwordhan");
        channelFileService.addUserChannel(codeReviewChannel.getChannelId(), userHan);
        System.out.println(channelFileService.findChannelById(codeReviewChannel.getChannelId()));

        System.out.println("\n채널 삭제 및 조회");
        channelFileService.removeChannelById(codeReviewChannel.getChannelId());
        System.out.println(channelFileService.getAllChannels());

        System.out.println("\n 채널 유저 초대");
        channelFileService.addUserChannel(talkingChannel.getChannelId(), userHan);
        channelFileService.addUserChannel(talkingChannel.getChannelId(), userJung);
        channelFileService.addUserChannel(talkingChannel.getChannelId(), userPark);
        System.out.println(channelFileService.findChannelById(talkingChannel.getChannelId()));

        System.out.println("\n 채널 유저 강퇴");
        channelFileService.kickUserChannel(talkingChannel.getChannelId(), userHan);
        System.out.println(channelFileService.findChannelById(talkingChannel.getChannelId()));

        System.out.println("\n 채널 방장 강퇴");
        channelFileService.kickUserChannel(talkingChannel.getChannelId(), userLee);
        System.out.println(channelFileService.findChannelById(talkingChannel.getChannelId()));

        System.out.println("\n 채널에 메세지 추가 및 조회");
        Message message = messageService.createMessage("안녕", "안녕하세요", userJung, userPark);
        Message message2 = messageService.createMessage("넵", "반갑습니다.", userPark, userJung);
        channelFileService.addMessageInCh(talkingChannel.getChannelId(), message);
        channelFileService.addMessageInCh(talkingChannel.getChannelId(), message2);
        System.out.println(channelFileService.findChannelById(talkingChannel.getChannelId()).getChannelMessages());

        System.out.println("\n 채널에 있는 \"안녕\" 메세지 삭제");
        channelFileService.removeMessageInCh(talkingChannel.getChannelId(), message);
        System.out.println(channelFileService.findChannelById(talkingChannel.getChannelId()).getChannelMessages());

        System.out.println("\n 채널에 있는 모든 메세지 조회");
        Map<UUID, Message> channelInMessageAll = channelFileService.findChannelInMessageAll(talkingChannel.getChannelId());
        System.out.println(channelInMessageAll);


        /**
         * @Description: FileIO와 객체 직렬화를 이용한 **메세지** 서비스 테스트
         */
        System.out.println("\n메세지 생성 및 조회");
        Message message3 = messageService.createMessage("안녕", "이건 메일 내용이야", userKim, userLee);
        System.out.println(messageService.getMessageById(message3.getMessageId()));

        System.out.println("\n모든 메세지 조회");
        System.out.println(messageService.getAllMessages());

        System.out.println("\n메세지 수정");
        messageService.updateMessage(message3.getMessageId(), "메세지 제목을 바꿈", "잘 바뀌지?");
        System.out.println(messageService.getMessageById(message3.getMessageId()));

        System.out.println("\n메세지 삭제");
        messageService.deleteMessage(message3.getMessageId());
        System.out.println(messageService.getAllMessages());


        /**
         * @Description: **BasicUserService** 테스트
         */
        System.out.println("\n\n--------------------Basic Service 테스트------------------------");
        System.out.println("유저 추가 및 조회");
        User basicUserKim = userService.createUser("kim", "kim@mail.com", "passwordkim");
        User basicUserLee = userService.createUser("lee", "lee@mail.com", "passwordlee");
        System.out.println(userService.getUserById(basicUserKim.getUserId()));
        System.out.println(userService.getUserById(basicUserLee.getUserId()));

        System.out.println("\n모든 유저 조회");
        System.out.println(userService.getAllUsers());

        System.out.println("\n \"kim\" 유저 수정");
        userService.updateUser(basicUserKim.getUserId(), "gim", "gim@mail.com", "passwordgim");
        System.out.println(userService.getUserById(basicUserKim.getUserId()));

        System.out.println("\n \"lee\" 유저 삭제");
        userService.deleteUser(basicUserLee.getUserId());
        System.out.println(userService.getAllUsers());


        /**
         * @Description: File **BasicChannelService** 테스트
         */
        System.out.println("\n채널 등록 및 조회");
        Map<UUID, User> userMap2 = new HashMap<>();
        userMap2.put(basicUserKim.getUserId(), basicUserKim);
        userMap2.put(basicUserLee.getUserId(), basicUserLee);
        Channel basicChannel = channelService.createChannel("code review", basicUserKim, userMap2);
        System.out.println(channelService.getAllChannels());

        System.out.println("\n채널 id로 조회");
        System.out.println(channelService.findChannelById(basicChannel.getChannelId()));

        System.out.println("\n채널 이름으로 조회");
        System.out.println(channelService.getChannelByName("code review"));

        System.out.println("\n모든 채널 조회");
        Channel basicChannel2 = channelService.createChannel("play with me game", basicUserLee, userMap2);
        System.out.println(channelService.getAllChannels());

        System.out.println("\n채널 변경");
        channelService.updateChannel(basicChannel2.getChannelId(), "game channel", basicUserKim);
        System.out.println(channelService.findChannelById(basicChannel2.getChannelId()));

        System.out.println("\n채널 삭제");
        channelService.removeChannelById(basicChannel2.getChannelId());
        System.out.println(channelService.getAllChannels());

        System.out.println("\n채널에 \"Han\" 초대");
        User basicUserHan = userService.createUser("han", "han@mail.com", "passwordhan");
        channelService.addUserChannel(basicChannel.getChannelId(), basicUserHan);
        System.out.println(channelService.findChannelById(basicChannel.getChannelId()));

        System.out.println("\n채널에 있는 \"Lee\" 강퇴");
        channelService.kickUserChannel(basicChannel.getChannelId(), basicUserLee);
        System.out.println(channelService.findChannelById(basicChannel.getChannelId()));

        System.out.println("\n채널 방장(Kim) 나가기");
        channelService.kickUserChannel(basicChannel.getChannelId(), basicUserKim);
        System.out.println(channelService.findChannelById(basicChannel.getChannelId()));

        System.out.println("\n채널에 메세지 추가 및 모든 조회");
        Message basicMessage = messageService.createMessage("안녕", "안녕하세요", basicUserKim, basicUserHan);
        Message basicMessage2 = messageService.createMessage("넵", "반갑습니다.", basicUserHan, basicUserKim);
        channelService.addMessageInCh(basicChannel.getChannelId(), basicMessage);
        channelService.addMessageInCh(basicChannel.getChannelId(), basicMessage2);
        System.out.println(channelService.findChannelInMessageAll(basicChannel.getChannelId()));

        System.out.println("\n채널에 있는 \"안녕\" 메세지 삭제");
        channelService.removeMessageInCh(basicChannel.getChannelId(), basicMessage);
        System.out.println(channelService.findChannelInMessageAll(basicChannel.getChannelId()));

        System.out.println("\n채널에 있는 모든 메세지 조회");
        Message basicMessage3 = messageService.createMessage("추가된 메세지", "하이하이.", basicUserHan, basicUserKim);
        channelService.addMessageInCh(basicChannel.getChannelId(), basicMessage3);
        System.out.println(channelService.findChannelInMessageAll(basicChannel.getChannelId()));



        /**
         * @Description: File **BasicMessageService** 테스트
         */
        System.out.println("\n메세지 생성 및 조회");
        Message basicMessage4 = messageService.createMessage("안녕", "안녕하세요", basicUserKim, basicUserHan);
        System.out.println(messageService.getMessageById(basicMessage4.getMessageId()));

        System.out.println("\n모든 메세지 조회");
        System.out.println(messageService.getAllMessages());

        System.out.println("\n메세지 수정");
        messageService.updateMessage(basicMessage4.getMessageId(), "메세지 제목을 바꿈", "잘 바뀌지?");
        System.out.println(messageService.getMessageById(basicMessage4.getMessageId()));

        System.out.println("\n메세지 삭제");
        messageService.deleteMessage(basicMessage4.getMessageId());
        System.out.println(messageService.getAllMessages());



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
