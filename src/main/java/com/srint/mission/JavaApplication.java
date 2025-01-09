package com.srint.mission;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.jcf.JCFChannelService;
import com.srint.mission.discodeit.service.jcf.JCFMessageService;
import com.srint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {


    public static void main(String[] args) {
        userTest();
        channelTest();
        messageTest();
    }

    private static void userTest(){

        JCFUserService userService = new JCFUserService();


        System.out.println("\n\nUser Test\n");


        //유저 생성하기
        User userA = new User("userA","userA@naver.com","passwordA");
        User userB = new User("userB","userB@naver.com","passwordB");
        User userC = new User("userC","userC@naver.com","passwordC");

        //등록
        userService.create(userA);
        userService.create(userB);
        userService.create(userC);

        //조회
        //단건
        User readA = userService.read(userA.getId());
        User readB = userService.read(userB.getId());
        User readC = userService.read(userC.getId());

        System.out.println("단건 조회하기");
        System.out.println(readA);
        System.out.println(readB);
        System.out.println(readC);
        System.out.println();

        //다건
        List<User> users = userService.readAll();

        System.out.println("다건 조회하기");
        for(User user : users){
            System.out.println(user);
        }
        System.out.println();

        //수정 (이메일 수정하기)
        System.out.println("수정 - 이메일 수정하기");
        userA.setEmail("updateUserA@naver.com");
        userService.update(userA.getId(), userA);

        //수정된 데이터 조회
        System.out.println("수정된 데이터 조회");
        User edituser = userService.read(userA.getId());
        System.out.println(edituser);
        System.out.println();

        //삭제
        userService.delete(userA.getId());

        //조회를 통해 삭제되었는지 확인
        System.out.println("다건 조회하기 - 삭제 확인");
        for(User user : userService.readAll()){
            System.out.println(user);
        }
        System.out.println();

    }

    public static void channelTest(){

        JCFUserService userService = new JCFUserService();

        System.out.println("\n\nChannel Test\n");

        //채널 생성
        User userA = new User("userA","userA@naver.com","passwordA");
        User userB = new User("userB","userB@naver.com","passwordB");
        User userC = new User("userC","userC@naver.com","passwordC");
        userService.create(userA);

        //등록
        JCFChannelService channelService = new JCFChannelService();

        Channel channelA = new Channel(userA.getId(), "channelByUserA");
        Channel channelB = new Channel(userB.getId(), "channelByUserB");
        Channel channelC = new Channel(userC.getId(), "channelByUserC");

        channelService.create(channelA);
        channelService.create(channelB);
        channelService.create(channelC);

        //조회
        System.out.println("단건 조회하기");
        Channel readchannel = channelService.read(channelA.getId());
        System.out.println(readchannel);
        System.out.println();

        //다건조회
        System.out.println("다건 조회하기");
        for(Channel channel : channelService.readAll()){
            System.out.println(channel);
        }
        System.out.println();


        //수정
        System.out.println("수정 - 채널이름 수정하기");
        channelA.setChannelname("editChannelA");
        channelService.update(channelA.getId(), channelA);

        //수정된 데이터 조회
        System.out.println("수정된 데이터 조회");
        Channel editChannel = channelService.read(channelA.getId());
        System.out.println(editChannel);
        System.out.println();

        //삭제
        channelService.delete(channelB.getId());

        //삭제 확인
        System.out.println("다건 조회하기 - 채널 삭제 확인");
        for(Channel channel : channelService.readAll()){
            System.out.println(channel);
        }
        System.out.println();

    }


    public static void messageTest(){

        System.out.println("\n\nMessage Test\n");


        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();

        User userA = new User("userA","userA@naver.com","passwordA");
        User userB = new User("userB","userB@naver.com","passwordB");
        User userC = new User("userC","userC@naver.com","passwordC");
        userService.create(userA);
        userService.create(userB);
        userService.create(userC);

        Channel channelA = new Channel(userA.getId(), "channelByUserA");
        Channel channelB = new Channel(userB.getId(), "channelByUserB");
        Channel channelC = new Channel(userC.getId(), "channelByUserC");
        channelService.create(channelA);
        channelService.create(channelB);
        channelService.create(channelC);


        //메시지 생성하기
        Message message1 = new Message(userA.getId(), channelA.getId(), "안녕A");
        Message message2 = new Message(userB.getId(), channelA.getId(), "안녕B");
        Message message3 = new Message(userA.getId(), channelA.getId(), "반가워A");
        Message message4 = new Message(userB.getId(), channelA.getId(), "반가워B");


        //등록
        JCFMessageService messageService = new JCFMessageService();
        messageService.create(message1);
        messageService.create(message2);
        messageService.create(message3);
        messageService.create(message4);

        //조회
        //단건
        Message readA = messageService.read(message1.getId());
        System.out.println("단건 조회하기");
        System.out.println(readA);
        System.out.println();

        //다건
        System.out.println("다건 조회하기");
        for(Message msg : messageService.readAll()){
            System.out.println(msg);
        }
        System.out.println();

        //수정 (메시지 수정하기)
        System.out.println("수정 - 메시지 수정하기");
        message1.setMsg("수정된 메시지");
        messageService.update(message1.getId(), message1);

        //수정된 데이터 조회
        System.out.println("수정된 데이터 조회");
        Message editMsg = messageService.read(message1.getId());
        System.out.println(editMsg);
        System.out.println();

        //삭제
        messageService.delete(message1.getId());

        //조회를 통해 삭제되었는지 확인
        for(Message msg : messageService.readAll()){
            System.out.println(msg);
        }
        System.out.println();

    }



}