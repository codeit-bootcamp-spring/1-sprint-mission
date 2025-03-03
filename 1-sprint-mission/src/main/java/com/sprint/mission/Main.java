package com.sprint.mission;

import com.sprint.mission.discodeit.service.basic.*;

public class Main {
    public static void main(String[] args) {

        BasicUserService u=new BasicUserService();

        //u.SetUpJfcUser();
        u.SetUpFileUser();

        System.out.println(u.readUserAll());
        u.createNewUser("고구마");
        u.createNewUser("김치");
        u.createNewUser("우유");
        System.out.println(u.readUserAll());
        System.out.println(u.readUser("고구마"));
        u.updateUserName("고구마","감자");
        System.out.println(u.readUser("고구마"));
        System.out.println(u.readUser("감자"));
        u.deleteUser("김치");



        BasicMessageService m=new BasicMessageService();
        //m.setUpJfcMessage();
        m.setUpFileMessage();

        System.out.println(m.readMessageAll());
        m.createNewMessage("안녕하세요","감자입니다");
        System.out.println(m.readMessageAll());
        System.out.println(m.readMessage("안녕하세요"));
        m.updateMessageTitle("반가워요","잘가요");
        m.updateMessageTitle("안녕하세요","잘가요");
        System.out.println(m.readMessage("안녕하세요"));
        System.out.println(m.readMessage("잘가요"));
        m.updateMessageTitle("잘가요","고구마에요");
        System.out.println(m.readMessage("잘가요"));
        m.deleteMessage("안녕하세요");
        m.deleteMessage("잘가요");
        System.out.println(m.readMessage("잘가요"));
        m.createNewMessage("잘 지내세요?","저 맛탕 되었어요");
        m.createNewMessage("마지막으로?","달달해요");
        m.createNewMessage("잘 지내세요?","다른 메세지");
        System.out.println(m.readMessageAll());



        BasicChannelService c=new BasicChannelService();

        //c.setUpJfcChannel();
        c.setUpFileChannel();

        System.out.println(c.readChannelAll());
        c.createNewChannel("탄수화물");
        System.out.println(c.readChannelAll());
        System.out.println(c.readChannel("탄수화물"));
        c.updateChannelName("탄수화물","뿌리식물");
        System.out.println(c.readChannel("탄수화물"));
        System.out.println(c.readChannel("뿌리식물"));
        c.deleteChannel("뿌리식물");
        System.out.println(c.readChannel("뿌리식물"));
        c.createNewChannel("구황작물");
        c.createNewChannel("튀기면 맛있는 작물");
        c.createNewChannel("구황작물");

        System.out.println(c.readChannelAll());



    }


}
