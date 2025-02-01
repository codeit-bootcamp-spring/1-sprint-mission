package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.*;
import com.sprint.mission.discodeit.service.file.*;
import com.sprint.mission.discodeit.service.basic.*;

import java.util.List;


public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("==== [User Service Test] ====");
        UserRepository jcfUserRepository = new JCFUserRepository();
        UserRepository fileUserRepository = new FileUserRepository();
        UserService userService = new BasicUserService(fileUserRepository);

        System.out.println("<User 생성>");
        User user1 = userService.createUser("임수빈", "1234", "qwer1234@gmail.com");
        User user2 = userService.createUser("허원재", "5678", "asdf5678@gmail.com");
        User user3 = userService.createUser("이규석", "9876", "uiop7890@gmail.com");
        System.out.println();

        System.out.println("<User 조회>\n- 단건 조회");
        User searchUser = userService.findUserById(user1);
        userService.printUser(searchUser);
        System.out.println("-다건 조회");
        List<User> userList = userService.findAllUsers();
        userService.printListUsers(userList);
        System.out.println();

        System.out.println("<User 정보 수정 및 확인>");
        System.out.println("- 이름 수정 (임수빈 -> ImSubin)");
        user1 = userService.updateUserName(user1, "ImSubin");
        searchUser = userService.findUserById(user1);
        userService.printUser(searchUser);
        System.out.println("- 비밀번호 수정 (5678 -> 4321)");
        user2 = userService.updatePassword(user2, "4321");
        searchUser = userService.findUserById(user2);
        userService.printUser(searchUser);
        System.out.println("- 이메일 수정 (uiop7890@gmail.com -> poiu0987@gmail.com)");
        user3 = userService.updateEmail(user3, "poiu0987@gmail.com");
        searchUser = userService.findUserById(user3);
        userService.printUser(searchUser);
        System.out.println();

        System.out.println("<User 삭제 및 확인>");
        userService.deleteUserById(user3);
        userList = userService.findAllUsers();
        userService.printListUsers(userList);
        System.out.println();

        System.out.println();
        System.out.println("==== [Channel Service Test] ====");
        ChannelRepository jcfChannelRepository = new JCFChannelRepository();
        ChannelRepository fileChannelRepository = new FileChannelRepository();
        ChannelService channelService = new BasicChannelService(fileChannelRepository);

        System.out.println("<Channel 생성>");
        Channel channel1 = channelService.createChannel("코드잇 스프린트", "코드잇");
        Channel channel2 = channelService.createChannel("스터디", "JAVA 스터디 그룹");
        System.out.println();

        System.out.println("<Channel 조회>\n- 단건 조회");
        Channel searchChannel = channelService.findChannel(channel1);
        channelService.printChannel(searchChannel);
        System.out.println("-다건 조회");
        List<Channel> channelList = channelService.findAllChannels();
        channelService.printAllChannels(channelList);
        System.out.println();

        System.out.println("<Channel 정보 수정 및 확인>");
        System.out.println("- 이름 수정 (스터디 -> JAVA 스터디)");
        channel2 = channelService.updateName(channel2, "JAVA 스터디");
        searchChannel = channelService.findChannel(channel2);
        channelService.printChannel(searchChannel);
        System.out.println("- 설명 수정 (코드잇 -> Spring 백엔드 1기");
        channel1 = channelService.updateDescription(channel1, "Spring 백엔드 1기");
        searchChannel = channelService.findChannel(channel1);
        channelService.printChannel(searchChannel);
        System.out.println();

        System.out.println("<Channel 삭제 및 확인>");
        channelService.deleteChannel(channel2);
        channelList = channelService.findAllChannels();
        channelService.printAllChannels(channelList);
        System.out.println();

        System.out.println();
        System.out.println("==== [Message Service Test] ====");
        MessageRepository jcfMessageRepository = new JCFMessageRepository();
        MessageRepository fileMessageRepository = new FileMessageRepository();
        MessageService messageService = new BasicMessageService(fileMessageRepository);

        System.out.println("<Message 생성>");
        Message message1 = messageService.createMessage("Hi", channel1, user1);
        Message message2 = messageService.createMessage("Thanks!", channel1, user1);
        Message message3 = messageService.createMessage("Hello~~", channel2, user2);
        System.out.println();

        System.out.println("<Message 조회>\n- 단건 조회");
        Message searchMessage = messageService.findMessageById(message1);
        messageService.printMessage(searchMessage);
        System.out.println("-채널별 조회");
        List<Message> messageList = messageService.findMessageByChannel(message1);
        messageService.printMessagesList(messageList);
        System.out.println("-전체 조회");
        messageList = messageService.findAllMessage();
        messageService.printMessagesList(messageList);
        System.out.println();

        System.out.println("<Message 수정 및 확인>");
        System.out.println("- 내용 수정 (Thanks! -> mission complete!)");
        message2 = messageService.updateContent(message2, "mission complete!");
        searchMessage = messageService.findMessageById(message2);
        messageService.printMessage(searchMessage);
        System.out.println();

        System.out.println("<Message 삭제 및 확인>");
        messageService.deleteMessage(message1);
        messageList = messageService.findAllMessage();
        messageService.printMessagesList(messageList);
        System.out.println();

    }
}
