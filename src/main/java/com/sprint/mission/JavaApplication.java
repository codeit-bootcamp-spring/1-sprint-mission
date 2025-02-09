package com.sprint.mission;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.UUID;



public class JavaApplication {
    public static void main(String[] args) {
        basicServiceTest();
    }

    public static void basicServiceTest(){
        UserService userService = new BasicUserService(new JCFUserRepository());
        ChannelService channelService = new BasicChannelService(new JCFChannelRepository());
        MessageService messageService = new BasicMessageService(new JCFMessageRepository());
        System.out.println("BasicServiceTest\n");

        //UserTest
        System.out.println("userTest\n\n\n");

        //등록
        UUID userId1 = userService.create("user1", "111@naver.com","abc123");
        UUID userId2 = userService.create("user2", "222@naver.com","abc123");
        UUID userId3 = userService.create("user3", "333@naver.com","abc123");

        //조회 - 단건
        User user1 = userService.read(userId1);
        System.out.println("단건 조회");
        System.out.println(user1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(User user : userService.readAll()) System.out.println(user);

        //수정
        userService.updateUser(userId1, "edit_name", "edit_email@naver.com");
        System.out.println();

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(user1);
        System.out.println();

        //삭제
        userService.deleteUser(userId1);

        //조회를 통해 삭제 확인
        //조회 - 다건
        System.out.println("다건 조회 - 삭제확인");
        for(User user : userService.readAll()) System.out.println(user);
        System.out.println();

        //ChannelTest
        //등록
        System.out.println("ChannelTest\n\n\n");
        UUID channelId1 = channelService.create("channel1", "채널 1입니다", ChannelType.PUBLIC);
        UUID channelId2 = channelService.create("channel2", "채널 2입니다", ChannelType.PUBLIC);
        UUID channelId3 = channelService.create("channel3", "채널 3입니다", ChannelType.PUBLIC);

        //조회 - 단건
        System.out.println("단건 조회");
        Channel channel1 = channelService.read(channelId1);
        System.out.println(channel1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(Channel channel : channelService.readAll()) System.out.println(channel);
        System.out.println();

        //수정
        channelService.updateChannel(channelId1, "수정한 채널 1이름", "수정한 채런 설명 1", ChannelType.PUBLIC);

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(channel1);
        System.out.println();

        //삭제
        channelService.deleteChannel(channelId1);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        for(Channel channel : channelService.readAll()) System.out.println(channel);
        System.out.println();


        //MessageTest
        System.out.println("MessageTest\n\n\n");
        //등록

        User findUser2 = userService.read(userId2);
        User findUser3 = userService.read(userId3);
        Channel findChannel2 = channelService.read(channelId2);
        Channel findChannel3 = channelService.read(channelId3);
        UUID messageId1 = messageService.create("메시지1", findUser2.getId(), findChannel2.getId());
        UUID messageId2 = messageService.create("메시지2", findUser3.getId(), findChannel2.getId());

        //조회 - 단건
        System.out.println("단건 조회");
        Message message1 = messageService.read(messageId1);
        System.out.println(message1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        messageService.readAll().forEach(System.out::println);
        System.out.println();

        //수정
        messageService.updateMessage(messageId1, "수정된 메시지1",findUser2);

        //수정된 데이터 조회
        System.out.println("수정된 메시지 조회");
        System.out.println(message1);
        System.out.println();

        //삭제
        messageService.deleteMessage(messageId1, findUser2);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        messageService.readAll().forEach(System.out::println);
        System.out.println();

    }
}

