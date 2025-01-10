package com.sprint.mission.discodeit.control;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCF_channel;
import com.sprint.mission.discodeit.service.jcf.JCF_message;
import com.sprint.mission.discodeit.service.jcf.JCF_user;

import java.util.List;
import java.util.Scanner;

public class MainControl {
    JCF_channel jcf_channel = new JCF_channel();
    JCF_user jcf_user = new JCF_user();
    JCF_message jcf_message = new JCF_message();
    Scanner sc = new Scanner(System.in);
    public void mainDisplay() {
        System.out.println("1. User \n2. Channel");
        int num = sc.nextInt();
        switch (num) {
            case 1:
                userDisplay();
                break;
            case 2:
                channelDisplay();
                break;
            default:
                System.out.println("Please enter again");
                mainDisplay();
                break;

        }

    }

    public void userDisplay() {
        System.out.println("USER");
        List<User> userList = jcf_user.allWrite();
        for (User item : userList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("1.Add User / 2.Update User / 3.Delete User / 4.Go to main");
        int num = sc.nextInt();
        switch (num) {
            case 1:
                addUser();
                break;
            case 2:
                updateUser();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                mainDisplay();
                break;
            default:
                userDisplay();
                break;

        }
    }

    public void addUser() {
        System.out.println("Input User name");
        String name = sc.next();
        jcf_user.creat(new User(name));
        userDisplay();
    }

    public void updateUser() {
        List<User> userList = jcf_user.allWrite();
        for (User item : userList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Which user will you edit (in number)?");
        int num = sc.nextInt() - 1;
        System.out.println("Input Update User name");
        String name = sc.next();
        jcf_user.update(userList.get(num), name);
        userDisplay();
    }

    public void deleteUser() {
        List<User> userList = jcf_user.allWrite();
        for (User item : userList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Which user do you want to delete (by number)");
        int num = sc.nextInt() - 1;
        jcf_message.deleteUserMessage(userList.get(num));
        jcf_user.delete(userList.get(num));
        userDisplay();
    }

    public void channelDisplay() {
        System.out.println("Channel");
        List<Channel> channelList = jcf_channel.allWrite();
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("1.Add Channel / 2.Update Channel / 3.Delete Channel / 4.Join the channel / 5.Go to main");
        int num = sc.nextInt();
        switch (num) {
            case 1:
                addChannel();
                break;
            case 2:
                updateChannel();
                break;
            case 3:
                deleteChannel();
                break;
            case 4:
                joinChannel();
                break;
            default:
                mainDisplay();
                break;

        }

    }
    public void addChannel() {
        System.out.println("Input Add Channel Title");
        String name = sc.next();
        jcf_channel.creat(new Channel(name));
        channelDisplay();
    }

    public void updateChannel() {
        List<Channel> channelList = jcf_channel.allWrite();
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Which channel will you edit (in number)?");
        int num = sc.nextInt() - 1;
        System.out.println("Input Update channel title");
        String name = sc.next();
        jcf_channel.update(channelList.get(num), name);
        channelDisplay();
    }
    public void deleteChannel() {
        List<Channel> channelList = jcf_channel.allWrite();
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Which channel do you want to delete (by number)");
        int num = sc.nextInt() - 1;
        jcf_message.deleteChannelMessage(channelList.get(num));
        jcf_channel.delete(channelList.get(num));
        channelDisplay();
    }
    public void joinChannel() {
        List<Channel> channelList = jcf_channel.allWrite();
        for (Channel item : channelList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Which channel will you be on (by number)?");
        int num = sc.nextInt() - 1;
        messageDisplay(channelList.get(num));
    }
    public void messageDisplay(Channel channel) {
        System.out.println(channel.getTitle() + " " + "Channel");
        List<Message> messageList = jcf_message.getMessage(channel.getTitle());
        for (Message item : messageList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("1.Add Message / 2.Update Message / 3.Delete Message / 4.Go to Channel");
        int num = sc.nextInt();
        switch (num) {
            case 1:
                addMessage(channel);
                break;
            case 2:
                updateMessage(channel);
                break;
            case 3:
                deleteMessage(channel);
                break;
            case 4:
                channelDisplay();
                break;
            default:
                messageDisplay(channel);
                System.out.println("Please enter again");
                break;

        }
    }
    public void addMessage(Channel channel) {
        List<Message> messageList = jcf_message.getMessage(channel.getTitle());
        for (Message item : messageList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        List<User> userList = jcf_user.allWrite();
        for (User item : userList) {
            System.out.println("- " + item.getName());
        }
        System.out.println("\n");
        System.out.println("Please enter your name");
        String name = sc.next();
        User creUser = jcf_user.write(name);
        System.out.println("Please enter chat");
        sc.nextLine();
        String content = sc.nextLine();
        jcf_message.creat(new Message(creUser, content, channel));
        messageDisplay(channel);
    }

    public void updateMessage(Channel channel) {
        List<Message> messageList = jcf_message.getMessage(channel.getTitle());
        for (Message item : messageList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Please enter the message you want to edit (by number)");
        int num = sc.nextInt() - 1;
        Message message = messageList.get(num);
        System.out.println("Please enter chat");
        sc.nextLine();
        String content = sc.nextLine();
        jcf_message.update(message, content);
        messageDisplay(channel);
    }

    public void deleteMessage(Channel channel) {
        List<Message> messageList = jcf_message.getMessage(channel.getTitle());
        for (Message item : messageList) {
            System.out.println("- " + item);
        }
        System.out.println("\n");
        System.out.println("Please enter the message you want to delete (by number)");
        int num = sc.nextInt() - 1;
        Message message = messageList.get(num);
        jcf_message.delete(message);
        messageDisplay(channel);
    }
}
