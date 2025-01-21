package com.srint.mission;


import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.jcf.JCFChannelService;
import com.srint.mission.discodeit.service.jcf.JCFMessageService;
import com.srint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

        JCFUserService jcfUserService = new JCFUserService();
        JCFChannelService jcfChannelService = new JCFChannelService();
        JCFMessageService jcfMessageService = new JCFMessageService();

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