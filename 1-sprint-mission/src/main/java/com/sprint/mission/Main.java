package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.serviece.*;

public class Main {
    public static void main(String[] args) {

        UserService u=new JCFUserService();

        System.out.println(u.ReadUserAll());
        u.CreateUser("고구마 ");
        System.out.println(u.ReadUserAll());
        System.out.println(u.ReadUser("고구마"));
        u.UpdateUserName("고구마","감자");
        System.out.println(u.ReadUser("고구마"));
        System.out.println(u.ReadUser("감자"));
        u.DeleteUser("고구마");
        u.DeleteUser("감자");
        System.out.println(u.ReadUser("감자"));




        MessageService m=new JCFMessageService();
        System.out.println(m.ReadMessageAll());
        m.CreateMessage("안녕하세요","감자입니다");
        System.out.println(m.ReadMessageAll());
        System.out.println(m.ReadMessage("안녕하세요"));
        m.UpdateMessageTitle("반가워요","잘가요");
        m.UpdateMessageTitle("안녕하세요","잘가요");
        System.out.println(m.ReadMessage("안녕하세요"));
        System.out.println(m.ReadMessage("잘가요"));
        m.UpdateMessageBody("잘가요","고구마에요");
        System.out.println(m.ReadMessage("잘가요"));
        m.DeleteMessage("안녕하세요");
        m.DeleteMessage("잘가요");
        System.out.println(m.ReadMessage("잘가요"));



        ChannelService c=new JCFChannelServiece();
        System.out.println(c.ReadChannelAll());
        c.CreateChaneel("탄수화물");
        System.out.println(c.ReadChannelAll());
        System.out.println(c.ReadChannel("탄수화물"));
        c.UpdateChannelName("탄수화물","뿌리식물");
        System.out.println(c.ReadChannel("탄수화물"));
        System.out.println(c.ReadChannel("뿌리식물"));
        c.DeleteChannel("뿌리식물");
        System.out.println(c.ReadChannel("뿌리식물"));




    }
}