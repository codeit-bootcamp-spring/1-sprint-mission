package com.srint.mission;


import com.srint.mission.discodeit.entity.Channel;
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
        //jcfServiceTest();
        //fileServiceTest();
        basicServiceTest();
    }

    public static void basicServiceTest(){
        UserService userService = new BasicUserService(new FileUserRepository());
        ChannelService channelService = new BasicChannelService(new FileChannelRepository());
        MessageService messageService = new BasicMessageService(new FileMessageRepository());

        System.out.println("jcfServiceTest\n");

        //UserTest
        System.out.println("userTest\n\n\n");

        //등록
        UUID id1 = userService.create("user1", "111@naver.com","abc123");
        UUID id2 = userService.create("user2", "222@naver.com","abc123");
        UUID id3 = userService.create("user3", "333@naver.com","abc123");
        UUID id4 = userService.create("user4", "444@naver.com","abc123");

        //조회 - 단건
        User user1 = userService.read(id1);
        System.out.println("단건 조회");
        System.out.println(user1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(User user : userService.readAll()) System.out.println(user);

        //수정
        userService.updateUserName(id1, "edit_name");
        userService.updateEmail(id2, "edit_email@naver.com");
        System.out.println();

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        User editUser1 = userService.read(id1);
        User editUser2 = userService.read(id2);
        System.out.println(editUser1);
        System.out.println(editUser2);
        System.out.println();

        //삭제
        userService.deleteUser(id1);

        //조회를 통해 삭제 확인
        //조회 - 다건
        System.out.println("다건 조회 - 삭제확인");
        for(User user : userService.readAll()) System.out.println(user);
        System.out.println();


        //------------데이터 추가
        UUID id5 = userService.create("user5", "555@naver.com","abc123");
        UUID id6 = userService.create("user6", "666@naver.com","abc123");
        User user5 = userService.read(id5);
        User user6 = userService.read(id6);
        //------------



        //ChannelTest
        //등록
        System.out.println("ChannelTest\n\n\n");
        User user3 = userService.read(id3);
        UUID cid1 = channelService.create("channel1", user3);

        User user4 = userService.read(id4);
        UUID cid2 = channelService.create("channel2", user4);

        //채널 가입
        channelService.joinChannel(cid1, user5);
        channelService.joinChannel(cid2, user5);
        channelService.joinChannel(cid2, user6);


        //조회 - 단건
        System.out.println("단건 조회");
        Channel channel1 = channelService.read(cid1);
        System.out.println(channel1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(Channel channel : channelService.readAll()) System.out.println(channel);
        System.out.println();

        //수정
        channelService.updateChannelName(cid1, user3, "editChannel1");

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(channel1);
        System.out.println();

        //가입 사용자 데이터 확인
        System.out.println("User별 가입한 채널 확인");
        user5.getMyChannels().forEach(System.out::println);
        System.out.println();

        //채널 탈회 - 회원
        System.out.println("채널 탈퇴 후 확인 - 회원");
        channelService.exitChannel(cid1, user5);
        user5.getMyChannels().forEach(System.out::println);
        System.out.println();

        //채널 탈퇴 - 채널
        System.out.println("채널 탈퇴 후 확인 - 채널");
        System.out.println(channel1);
        System.out.println();

        //삭제
        channelService.deleteChannel(cid1, user3);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        for(Channel channel : channelService.readAll()) System.out.println(channel);
        System.out.println();

        //---- 데이터 추가-----//
        UUID id10 = userService.create("user10", "1010@naver.com","abc123");
        UUID id11 = userService.create("user11", "1111@naver.com","abc123");
        UUID id12 = userService.create("user12", "1212@naver.com","abc123");
        User user10 = userService.read(id10);
        User user11 = userService.read(id11);
        User user12 = userService.read(id12);


        UUID cid10 = channelService.create("channel10", user10);
        UUID cid11 = channelService.create("channel11", user11);
        Channel channel10 = channelService.read(cid10);
        Channel channel11 = channelService.read(cid11);

        channelService.joinChannel(cid11, user12);

        //MessageTest
        System.out.println("MessageTest\n\n\n");
        //등록
        UUID mid1 = messageService.create("메시지1", user10, channel10);
        UUID mid2 = messageService.create("메시지2", user10, channel10);
        UUID mid3 = messageService.create("메시지3", user11, channel11);
        UUID mid4 = messageService.create("메시지4", user11, channel11);
        UUID mid5 =messageService.create("메시지5",user12, channel11);
        UUID mid6 =messageService.create("메시지6",user12, channel11);

        //조회 - 단건
        System.out.println("단건 조회");
        Message message1 = messageService.read(mid1);
        System.out.println(message1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        messageService.readAll().forEach(System.out::println);
        System.out.println();

        //수정
        Message editMessage = messageService.update(mid1, "수정된 메시지입니다.", user10);

        //수정된 데이터 조회
        System.out.println("수정된 메시지 조회");
        System.out.println(editMessage);
        System.out.println();

        //삭제 전 채널에 메시지 확인
        System.out.println("삭제 전 채널에서 메시지 확인");
        channel11.getMessages().forEach(System.out::println);
        System.out.println();

        //삭제
        messageService.deleteMessage(mid6, user12);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        messageService.readAll().forEach(System.out::println);
        System.out.println();

        //삭제 후 채널에서 메시지 확인
        System.out.println("삭제 후 채널에서 메시지 확인");
        channel11.getMessages().forEach(System.out::println);
        System.out.println();


    }

    public static void fileServiceTest(){
        FileUserService fileUserService = new FileUserService();
        FileChannelService fileChannelService = new FileChannelService();
        FileMessageService fileMessageService = new FileMessageService();


        fileUserService.clearFile();
        fileMessageService.clearFile();
        fileChannelService.clearFile();

        System.out.println("fileServiceTest\n");

        //UserTest
        System.out.println("userTest\n\n\n");

        //등록
        UUID id1 = fileUserService.create("user1", "111@naver.com","abc123");
        UUID id2 = fileUserService.create("user2", "222@naver.com","abc123");
        UUID id3 = fileUserService.create("user3", "333@naver.com","abc123");
        UUID id4 = fileUserService.create("user4", "444@naver.com","abc123");

        //조회 - 단건
        User user1 = fileUserService.read(id1);
        System.out.println("단건 조회");
        System.out.println(user1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(User user : fileUserService.findAll()) System.out.println(user);

        //수정
        fileUserService.updateUserName(id1, "edit_name");
        fileUserService.updateEmail(id2, "edit_email@naver.com");
        System.out.println();

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        User editUser1 = fileUserService.findOne(id1);
        User editUser2 = fileUserService.findOne(id2);
        System.out.println(editUser1);
        System.out.println(editUser2);
        System.out.println();

        //삭제
        fileUserService.delete(id1);

        //조회를 통해 삭제 확인
        //조회 - 다건
        System.out.println("다건 조회 - 삭제확인");
        for(User user : fileUserService.findAll()) System.out.println(user);
        System.out.println();


        //------------데이터 추가
        UUID id5 = fileUserService.create("user5", "555@naver.com","abc123");
        UUID id6 = fileUserService.create("user6", "666@naver.com","abc123");
        User user5 = fileUserService.findOne(id5);
        User user6 = fileUserService.findOne(id6);
        //------------



        //ChannelTest
        //등록
        System.out.println("ChannelTest\n\n\n");
        User user3 = fileUserService.findOne(id3);
        UUID cid1 = fileChannelService.create("channel1", user3);

        User user4 = fileUserService.findOne(id4);
        UUID cid2 = fileChannelService.create("channel2", user4);

        //채널 가입
        fileChannelService.joinChannel(cid1, user5);
        fileChannelService.joinChannel(cid2, user5);
        fileChannelService.joinChannel(cid2, user6);


        //조회 - 단건
        System.out.println("단건 조회");
        Channel channel1 = fileChannelService.findOne(cid1);
        System.out.println(channel1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(Channel channel : fileChannelService.findAll()) System.out.println(channel);
        System.out.println();

        //수정
        fileChannelService.updateChannelName(cid1, user3, "editChannel1");

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(channel1);
        System.out.println();

        //가입 사용자 데이터 확인
        System.out.println("User별 가입한 채널 확인");
        user5.getMyChannels().forEach(System.out::println);
        System.out.println();

        //채널 탈회 - 회원
        System.out.println("채널 탈퇴 후 확인 - 회원");
        fileChannelService.exitChannel(cid1, user5);
        user5.getMyChannels().forEach(System.out::println);
        System.out.println();

        //채널 탈퇴 - 채널
        System.out.println("채널 탈퇴 후 확인 - 채널");
        System.out.println(channel1);
        System.out.println();

        //삭제
        fileChannelService.deleteChannel(cid1, user3);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        for(Channel channel : fileChannelService.findAll()) System.out.println(channel);
        System.out.println();

        //---- 데이터 추가-----//
        UUID id10 = fileUserService.create("user10", "1010@naver.com","abc123");
        UUID id11 = fileUserService.create("user11", "1111@naver.com","abc123");
        UUID id12 = fileUserService.create("user12", "1212@naver.com","abc123");
        User user10 = fileUserService.findOne(id10);
        User user11 = fileUserService.findOne(id11);
        User user12 = fileUserService.findOne(id12);


        UUID cid10 = fileChannelService.create("channel10", user10);
        UUID cid11 = fileChannelService.create("channel11", user11);
        Channel channel10 = fileChannelService.findOne(cid10);
        Channel channel11 = fileChannelService.findOne(cid11);

        fileChannelService.joinChannel(cid11, user12);

        //MessageTest
        System.out.println("MessageTest\n\n\n");
        //등록
        UUID mid1 = fileMessageService.create("메시지1", user10, channel10);
        UUID mid2 = fileMessageService.create("메시지2", user10, channel10);
        UUID mid3 = fileMessageService.create("메시지3", user11, channel11);
        UUID mid4 = fileMessageService.create("메시지4", user11, channel11);
        UUID mid5 =fileMessageService.create("메시지5",user12, channel11);
        UUID mid6 =fileMessageService.create("메시지6",user12, channel11);

        //조회 - 단건
        System.out.println("단건 조회");
        Message message1 = fileMessageService.findOne(mid1);
        System.out.println(message1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        fileMessageService.findAll().forEach(System.out::println);
        System.out.println();

        //수정
        Message editMessage = fileMessageService.update(mid1, "수정된 메시지입니다.", user10);

        //수정된 데이터 조회
        System.out.println("수정된 메시지 조회");
        System.out.println(editMessage);
        System.out.println();

        //삭제 전 채널에 메시지 확인
        System.out.println("삭제 전 채널에서 메시지 확인");
        channel11.getMessages().forEach(System.out::println);
        System.out.println();

        //삭제
        fileMessageService.deleteMessage(mid6, user12);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        fileMessageService.findAll().forEach(System.out::println);
        System.out.println();

        //삭제 후 채널에서 메시지 확인
        System.out.println("삭제 후 채널에서 메시지 확인");
        channel11.getMessages().forEach(System.out::println);
        System.out.println();

        fileUserService.deleteFile();
        fileMessageService.deleteFile();
        fileChannelService.deleteFile();

    }

    public static void jcfServiceTest(){
        JCFUserService jcfUserService = new JCFUserService();
        JCFChannelService jcfChannelService = new JCFChannelService();
        JCFMessageService jcfMessageService = new JCFMessageService();

        System.out.println("jcfServiceTest\n");

        //UserTest
        System.out.println("userTest\n\n\n");

        //등록
        UUID id1 = jcfUserService.create("user1", "111@naver.com","abc123");
        UUID id2 = jcfUserService.create("user2", "222@naver.com","abc123");
        UUID id3 = jcfUserService.create("user3", "333@naver.com","abc123");
        UUID id4 = jcfUserService.create("user4", "444@naver.com","abc123");

        //조회 - 단건
        User user1 = jcfUserService.read(id1);
        System.out.println("단건 조회");
        System.out.println(user1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(User user : jcfUserService.findAll()) System.out.println(user);

        //수정
        jcfUserService.updateUserName(id1, "edit_name");
        jcfUserService.updateEmail(id2, "edit_email@naver.com");
        System.out.println();

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        User editUser1 = jcfUserService.findOne(id1);
        User editUser2 = jcfUserService.findOne(id2);
        System.out.println(editUser1);
        System.out.println(editUser2);
        System.out.println();

        //삭제
        jcfUserService.delete(id1);

        //조회를 통해 삭제 확인
        //조회 - 다건
        System.out.println("다건 조회 - 삭제확인");
        for(User user : jcfUserService.findAll()) System.out.println(user);
        System.out.println();


        //------------데이터 추가
        UUID id5 = jcfUserService.create("user5", "555@naver.com","abc123");
        UUID id6 = jcfUserService.create("user6", "666@naver.com","abc123");
        User user5 = jcfUserService.findOne(id5);
        User user6 = jcfUserService.findOne(id6);
        //------------



        //ChannelTest
        //등록
        System.out.println("ChannelTest\n\n\n");
        User user3 = jcfUserService.findOne(id3);
        UUID cid1 = jcfChannelService.create("channel1", user3);

        User user4 = jcfUserService.findOne(id4);
        UUID cid2 = jcfChannelService.create("channel2", user4);

        //채널 가입
        jcfChannelService.joinChannel(cid1, user5);
        jcfChannelService.joinChannel(cid2, user5);
        jcfChannelService.joinChannel(cid2, user6);


        //조회 - 단건
        System.out.println("단건 조회");
        Channel channel1 = jcfChannelService.findOne(cid1);
        System.out.println(channel1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        for(Channel channel : jcfChannelService.findAll()) System.out.println(channel);
        System.out.println();

        //수정
        jcfChannelService.updateChannelName(cid1, user3, "editChannel1");

        //수정된 데이터 조회
        System.out.println("수정 데이터 조회");
        System.out.println(channel1);
        System.out.println();

        //가입 사용자 데이터 확인
        System.out.println("User별 가입한 채널 확인");
        user5.getMyChannels().forEach(System.out::println);
        System.out.println();

        //채널 탈회 - 회원
        System.out.println("채널 탈퇴 후 확인 - 회원");
        jcfChannelService.exitChannel(cid1, user5);
        user5.getMyChannels().forEach(System.out::println);
        System.out.println();

        //채널 탈퇴 - 채널
        System.out.println("채널 탈퇴 후 확인 - 채널");
        System.out.println(channel1);
        System.out.println();

        //삭제
        jcfChannelService.deleteChannel(cid1, user3);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        for(Channel channel : jcfChannelService.findAll()) System.out.println(channel);
        System.out.println();

        //---- 데이터 추가-----//
        UUID id10 = jcfUserService.create("user10", "1010@naver.com","abc123");
        UUID id11 = jcfUserService.create("user11", "1111@naver.com","abc123");
        UUID id12 = jcfUserService.create("user12", "1212@naver.com","abc123");
        User user10 = jcfUserService.findOne(id10);
        User user11 = jcfUserService.findOne(id11);
        User user12 = jcfUserService.findOne(id12);


        UUID cid10 = jcfChannelService.create("channel10", user10);
        UUID cid11 = jcfChannelService.create("channel11", user11);
        Channel channel10 = jcfChannelService.findOne(cid10);
        Channel channel11 = jcfChannelService.findOne(cid11);

        jcfChannelService.joinChannel(cid11, user12);

        //MessageTest
        System.out.println("MessageTest\n\n\n");
        //등록
        UUID mid1 = jcfMessageService.create("메시지1", user10, channel10);
        UUID mid2 = jcfMessageService.create("메시지2", user10, channel10);
        UUID mid3 = jcfMessageService.create("메시지3", user11, channel11);
        UUID mid4 = jcfMessageService.create("메시지4", user11, channel11);
        UUID mid5 =jcfMessageService.create("메시지5",user12, channel11);
        UUID mid6 =jcfMessageService.create("메시지6",user12, channel11);

        //조회 - 단건
        System.out.println("단건 조회");
        Message message1 = jcfMessageService.findOne(mid1);
        System.out.println(message1);
        System.out.println();

        //조회 - 다건
        System.out.println("다건 조회");
        jcfMessageService.findAll().forEach(System.out::println);
        System.out.println();

        //수정
        Message editMessage = jcfMessageService.update(mid1, "수정된 메시지입니다.", user10);

        //수정된 데이터 조회
        System.out.println("수정된 메시지 조회");
        System.out.println(editMessage);
        System.out.println();

        //삭제 전 채널에 메시지 확인
        System.out.println("삭제 전 채널에서 메시지 확인");
        channel11.getMessages().forEach(System.out::println);
        System.out.println();

        //삭제
        jcfMessageService.deleteMessage(mid6, user12);

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회 - 삭제 확인");
        jcfMessageService.findAll().forEach(System.out::println);
        System.out.println();

        //삭제 후 채널에서 메시지 확인
        System.out.println("삭제 후 채널에서 메시지 확인");
        channel11.getMessages().forEach(System.out::println);
        System.out.println();
    }

}