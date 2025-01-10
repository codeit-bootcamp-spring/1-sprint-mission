package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();
        System.out.println("=== [User] ===");
        System.out.println("Create New User");
        User user1 = userService.createUser("Alice", "Alice1234@naver.com");
        User user2 = userService.createUser("Hazel", "Hazel0109@naver.com");
        System.out.println();

        System.out.println("[All Users]");
        List<User> getAllUser = userService.getAllUserList();
        userService.printUserListInfo(getAllUser);
        System.out.println();

        System.out.print("Read only one User : ");
        User searchUser = userService.searchById(user1.getId());
        userService.printUserInfo(searchUser);
        System.out.println();

        System.out.print("Updated User Name, "); //유저 이름 변경
        userService.updateUserName(user2, "Winter");
        userService.printUserInfo(user2);

        System.out.print("Updated User Email, "); //유저 이메일 변경
        userService.updateUserEmail(user1, "AliceUpdated@naver.com");
        userService.printUserInfo(user1);
        System.out.println();

        System.out.print("Deleted User, ");
        userService.deleteUser(user2);
        List<User> getAllUser2 = userService.getAllUserList();
        userService.printUserListInfo(getAllUser2);
        System.out.println();


        System.out.println("=== [Channel] ===");
        System.out.println("Create New Channel");
        Channel channel1 = channelService.createChannel("Algorithm Study from codeit", user1);
        Channel channel2 = channelService.createChannel("CodingTest Study from codeit", user1);
        Channel channel3 = channelService.createChannel("Java Study from codeit", user2);
        System.out.println();

        System.out.println("[All Channels]");
        List<Channel> channelList = channelService.getAllChannelList();
        channelService.printChannelListInfo(channelList);
        System.out.println();

        System.out.print("Read one Channel : ");
        Channel searchChannel = channelService.searchById(channel1.getId());
        channelService.printChannelInfo(searchChannel);
        System.out.println();

        System.out.print("Updated Channel, ");
        channelService.updateTitle(channel1, "Running");
        channelService.printChannelInfo(channel1);
        System.out.println();

        System.out.print("Deleted Channel, ");
        channelService.deleteChannel(channel2);
        List<Channel> channelList2 = channelService.getAllChannelList();
        channelService.printChannelListInfo(channelList2);
        System.out.println();


        System.out.println("=== [Message] ===");
        System.out.println("Create New Message");
        Message message = messageService.createMessage(channel1, user1, "First Message for you");
        Message message2 = messageService.createMessage(channel1, user1, "I think it's too difficult");
        Message message3 = messageService.createMessage(channel2, user1, "Are you have been to study Java?");
        System.out.println();

        System.out.println("[All Messages]");
        List<Message> messageList = messageService.getAllMessageList();
        messageService.printMessageListInfo(messageList);
        System.out.println();

        System.out.print("Read one Message : ");
        Message searchMessage =  messageService.searchById(message.getId());
        messageService.printMessageInfo(searchMessage);
        System.out.println();

        System.out.print("Updated Message, ");
        messageService.updateMessage(message2, "I want to be Backend developer");
        messageService.printMessageInfo(message2);
        System.out.println();

        System.out.print("Message Deleted, ");
        messageService.deleteMessage(message);
        List<Message> messageList2 = messageService.getAllMessageList();
        messageService.printMessageListInfo(messageList2);
        System.out.println();

    }
}