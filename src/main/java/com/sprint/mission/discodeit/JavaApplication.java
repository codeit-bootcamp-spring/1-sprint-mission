package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.control.MainControl;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCF_channel;
import com.sprint.mission.discodeit.service.jcf.JCF_message;
import com.sprint.mission.discodeit.service.jcf.JCF_user;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class JavaApplication {

    public static void main(String[] args) {

        JCF_channel jcf_channel = new JCF_channel();
        JCF_user jcf_user = new JCF_user();
        JCF_message jcf_message = new JCF_message();


        System.out.println("유저 생성");
        jcf_user.creat(new User("user1"));
        jcf_user.creat(new User("user2"));
        jcf_user.creat(new User("user3"));
        jcf_user.creat(new User("user4"));
        List<User> userList = jcf_user.allWrite();
        for (User item : userList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("채널 생성");
        jcf_channel.creat(new Channel("channel1"));
        jcf_channel.creat(new Channel("channel2"));
        jcf_channel.creat(new Channel("channel3"));
        jcf_channel.creat(new Channel("channel4"));
        List<Channel> channelList = jcf_channel.allWrite();
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("채널에 채팅 생성");
        User user1 = jcf_user.write("user1");
        User user2 = jcf_user.write("user2");
        User user3 = jcf_user.write("user3");
        Channel channel = jcf_channel.write("channel1");
        jcf_message.creat(new Message(user1, "Message Test1!!", channel));
        jcf_message.creat(new Message(user2, "Message Test2!!", channel));
        jcf_message.creat(new Message(user3, "Message Test3!!", channel));

        List<Message> messageList = jcf_message.getMessage(channel.getTitle());
        for (Message item : messageList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("유저 수정");
        jcf_user.update(user1 ,"updateUser");
        for (User item : userList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("채널 수정");
        jcf_channel.update(channel, "UpdateChannel");
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("메세지 수정");
        jcf_message.update(messageList.get(0), "Update Message Test1!!");
        for (Message item : messageList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("유저 삭제");
        jcf_message.deleteUserMessage(user1);
        jcf_user.delete(user1);
        for (User item : userList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");

        System.out.println("채널 삭제");
        jcf_message.deleteChannelMessage(channel);
        jcf_channel.delete(channel);
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }


//        JCF_channel jcf_channel = new JCF_channel();
//        JCF_user jcf_user = new JCF_user();
//        JCF_message jcf_message = new JCF_message();
//
//
//
//        //채널 생성 채널 이름이 중복이라면 저장되지 않음
//        jcf_channel.Creat(new Channel("TEST"));
//        jcf_channel.Creat(new Channel("TEST3"));
//        Channel channel2 = jcf_channel.Write("TEST");
//
//
//        //유저 생성 /이름이 중복이라면 저장이 되지 않음
//        User creUser = jcf_user.Write("admin");
//        System.out.println(creUser.GetName());
//
//
//        //메세지 생성
//        String messsge_content = "Hello world!";
//        Message message = new Message(creUser, messsge_content, channel2);
//        jcf_message.Creat(message);
//
//        //채널 수정 중복 X
//        jcf_channel.Update(channel2, "TEST2");
//        List<Message> message1 = jcf_message.Write(creUser, "TEST2");
//
//        // 유저 수정
//        jcf_user.Update(creUser, "player");
//
//        //유저 삭제 (유저가 작성한 메세지도 삭제)
//        jcf_message.DeleteUserMessage(creUser);
//        jcf_user.Delete(creUser);
//
//
//        //채널 삭제 (채널에 있는 메세지도 삭제)
//        jcf_message.DeleteChannelMessage(channel2);
//        jcf_channel.Delete(channel2);
//        System.out.println(jcf_channel.AllWrite().toString());

    }
}
