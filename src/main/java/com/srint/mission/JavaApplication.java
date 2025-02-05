package com.srint.mission;


import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.ChannelType;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.file.FileChannelRepository;
import com.srint.mission.discodeit.repository.file.FileMessageRepository;
import com.srint.mission.discodeit.repository.file.FileUserRepository;
import com.srint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.srint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.srint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.srint.mission.discodeit.service.ChannelService;
import com.srint.mission.discodeit.service.MessageService;
import com.srint.mission.discodeit.service.UserService;
import com.srint.mission.discodeit.service.basic.BasicChannelService;
import com.srint.mission.discodeit.service.basic.BasicMessageService;
import com.srint.mission.discodeit.service.basic.BasicUserService;
import com.srint.mission.discodeit.service.file.FileMessageService;
import com.srint.mission.discodeit.service.file.FileUserService;
import com.srint.mission.discodeit.service.file.FileChannelService;
import com.srint.mission.discodeit.service.jcf.JCFChannelService;
import com.srint.mission.discodeit.service.jcf.JCFMessageService;
import com.srint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        jcfServiceTest();
        fileServiceTest();
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
        userService.updateUserName(userId1, "edit_name");
        userService.updateEmail(userId1, "edit_email@naver.com");
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
        channelService.updateName(channelId1, "수정된 채널 1 이름");
        channelService.updateDescription(channelId1, "수정된 채널 설명 1");
        channelService.updateType(channelId1, ChannelType.PRIVATE);

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

    public static void fileServiceTest(){
        FileUserService fileUserService = new FileUserService();
        FileChannelService fileChannelService = new FileChannelService();
        FileMessageService fileMessageService = new FileMessageService();

        fileUserService.clearFile();
        fileMessageService.clearFile();
        fileChannelService.clearFile();
        System.out.println("FileServiceTest(서비스+레포 같이 있는 서비스)\n");

        //UserTest
        System.out.println("userTest\n\n\n");

        //등록
        UUID userId1 = fileUserService.create("user1", "111@naver.com","abc123");
        UUID userId2 = fileUserService.create("user2", "222@naver.com","abc123");
        UUID userId3 = fileUserService.create("user3", "333@naver.com","abc123");

        //조회 - 단건
        User user1 = fileUserService.read(userId1);
        System.out.println("단건 조회");
        System.out.println(user1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(User user : fileUserService.readAll()) System.out.println(user);

        //수정
        fileUserService.updateUserName(userId1, "edit_name");
        fileUserService.updateEmail(userId1, "edit_email@naver.com");
        System.out.println();

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(user1);
        System.out.println();

        //삭제
        fileUserService.deleteUser(userId1);

        //조회를 통해 삭제 확인
        //조회 - 다건
        System.out.println("다건 조회 - 삭제확인");
        for(User user : fileUserService.readAll()) System.out.println(user);
        System.out.println();


        //ChannelTest
        //등록
        System.out.println("ChannelTest\n\n\n");
        UUID channelId1 = fileChannelService.create("channel1", "채널 1입니다", ChannelType.PUBLIC);
        UUID channelId2 = fileChannelService.create("channel2", "채널 2입니다", ChannelType.PUBLIC);
        UUID channelId3 = fileChannelService.create("channel3", "채널 3입니다", ChannelType.PUBLIC);

        //조회 - 단건
        System.out.println("단건 조회");
        Channel channel1 = fileChannelService.read(channelId1);
        System.out.println(channel1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(Channel channel : fileChannelService.readAll()) System.out.println(channel);
        System.out.println();

        //수정
        fileChannelService.updateName(channelId1, "수정된 채널 1 이름");
        fileChannelService.updateDescription(channelId1, "수정된 채널 설명 1");
        fileChannelService.updateType(channelId1, ChannelType.PRIVATE);

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(channel1);
        System.out.println();

        //삭제
        fileChannelService.deleteChannel(channelId1);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        for(Channel channel : fileChannelService.readAll()) System.out.println(channel);
        System.out.println();


        //MessageTest
        System.out.println("MessageTest\n\n\n");

        //등록
        User findUser2 = fileUserService.read(userId2);
        User findUser3 = fileUserService.read(userId3);
        Channel findChannel2 = fileChannelService.read(channelId2);
        Channel findChannel3 = fileChannelService.read(channelId3);
        UUID messageId1 = fileMessageService.create("메시지1", findUser2.getId(), findChannel2.getId());
        UUID messageId2 = fileMessageService.create("메시지2", findUser3.getId(), findChannel2.getId());

        //조회 - 단건
        System.out.println("단건 조회");
        Message message1 = fileMessageService.read(messageId1);
        System.out.println(message1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        fileMessageService.readAll().forEach(System.out::println);
        System.out.println();

        //수정
        fileMessageService.updateMessage(messageId1, "수정된 메시지1",findUser2);

        //수정된 데이터 조회
        System.out.println("수정된 메시지 조회");
        System.out.println(message1);
        System.out.println();

        //삭제
        fileMessageService.deleteMessage(messageId1, findUser2);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        fileMessageService.readAll().forEach(System.out::println);
        System.out.println();

        fileUserService.deleteFile();
        fileMessageService.deleteFile();
        fileChannelService.deleteFile();

    }



    public static void jcfServiceTest(){
        JCFUserService jcfUserService = new JCFUserService();
        JCFChannelService jcfChannelService = new JCFChannelService();
        JCFMessageService jcfMessageService = new JCFMessageService();

        System.out.println("jcfServiceTest(서비스+레포 같이 있는 서비스)\n");

        //UserTest
        System.out.println("userTest\n\n\n");

        //등록
        UUID userId1 = jcfUserService.create("user1", "111@naver.com","abc123");
        UUID userId2 = jcfUserService.create("user2", "222@naver.com","abc123");
        UUID userId3 = jcfUserService.create("user3", "333@naver.com","abc123");

        //조회 - 단건
        User user1 = jcfUserService.read(userId1);
        System.out.println("단건 조회");
        System.out.println(user1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(User user : jcfUserService.readAll()) System.out.println(user);

        //수정
        jcfUserService.updateUserName(userId1, "edit_name");
        jcfUserService.updateEmail(userId1, "edit_email@naver.com");
        System.out.println();

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(user1);
        System.out.println();

        //삭제
        jcfUserService.deleteUser(userId1);

        //조회를 통해 삭제 확인
        //조회 - 다건
        System.out.println("다건 조회 - 삭제확인");
        for(User user : jcfUserService.readAll()) System.out.println(user);
        System.out.println();


        //ChannelTest
        //등록
        System.out.println("ChannelTest\n\n\n");
        UUID channelId1 = jcfChannelService.create("channel1", "채널 1입니다", ChannelType.PUBLIC);
        UUID channelId2 = jcfChannelService.create("channel2", "채널 2입니다", ChannelType.PUBLIC);
        UUID channelId3 = jcfChannelService.create("channel3", "채널 3입니다", ChannelType.PUBLIC);

        //조회 - 단건
        System.out.println("단건 조회");
        Channel channel1 = jcfChannelService.read(channelId1);
        System.out.println(channel1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(Channel channel : jcfChannelService.readAll()) System.out.println(channel);
        System.out.println();

        //수정
        jcfChannelService.updateName(channelId1, "수정된 채널 1 이름");
        jcfChannelService.updateDescription(channelId1, "수정된 채널 설명 1");
        jcfChannelService.updateType(channelId1, ChannelType.PRIVATE);

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(channel1);
        System.out.println();

        //삭제
        jcfChannelService.deleteChannel(channelId1);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        for(Channel channel : jcfChannelService.readAll()) System.out.println(channel);
        System.out.println();


        //MessageTest
        System.out.println("MessageTest\n\n\n");

        //등록
        User findUser2 = jcfUserService.read(userId2);
        User findUser3 = jcfUserService.read(userId3);
        Channel findChannel2 = jcfChannelService.read(channelId2);
        Channel findChannel3 = jcfChannelService.read(channelId3);
        UUID messageId1 = jcfMessageService.create("메시지1", findUser2.getId(), findChannel2.getId());
        UUID messageId2 = jcfMessageService.create("메시지2", findUser3.getId(), findChannel2.getId());

        //조회 - 단건
        System.out.println("단건 조회");
        Message message1 = jcfMessageService.read(messageId1);
        System.out.println(message1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        jcfMessageService.readAll().forEach(System.out::println);
        System.out.println();

        //수정
        jcfMessageService.updateMessage(messageId1, "수정된 메시지1",findUser2);

        //수정된 데이터 조회
        System.out.println("수정된 메시지 조회");
        System.out.println(message1);
        System.out.println();

        //삭제
        jcfMessageService.deleteMessage(messageId1, findUser2);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        jcfMessageService.readAll().forEach(System.out::println);
        System.out.println();
    }
}