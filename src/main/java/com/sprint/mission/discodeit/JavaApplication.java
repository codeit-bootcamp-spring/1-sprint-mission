package com.sprint.mission.discodeit;

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
        UUID user1 = jcf_user.write("user1");
        UUID user2 = jcf_user.write("user2");
        UUID user3 = jcf_user.write("user3");
        UUID channel = jcf_channel.write("channel1");
        String userName1 = jcf_user.getName(user1);
        String userName2 = jcf_user.getName(user2);
        String userName3 = jcf_user.getName(user3);
        jcf_message.creat(new Message(user1, "Message Test1!!", channel, userName1));
        jcf_message.creat(new Message(user2, "Message Test2!!", channel, userName2));
        jcf_message.creat(new Message(user3, "Message Test3!!", channel, userName3));

        List<Message> messageList = jcf_message.getMessage(channel);
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
        jcf_message.update(messageList.get(0).getId(), "Update Message Test1!!");
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


    }
}
