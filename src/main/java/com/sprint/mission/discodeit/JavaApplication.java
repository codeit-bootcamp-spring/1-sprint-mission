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
        System.out.println("유저 생성");
        User userBob = userService.createUser("Bob", "qwe123@google.com");
        User userJackson = userService.createUser("Jackson", "Jackson@google.com");
        System.out.println();

        System.out.println("전체 조회");
        List<User> getAllUser = userService.getAllUserList();
        userService.printUserListInfo(getAllUser);
        System.out.println();

        System.out.println("단건 조회");
        User searchUser = userService.searchById(userBob.getId());
        userService.printUserInfo(searchUser);
        System.out.println();

        System.out.println("유저 이메일 수정 후 확인");
        userService.updateUserEmail(userBob, "bob@gmail.com");
        userService.printUserInfo(userBob);
        System.out.println();

        System.out.println("유저 삭제 후 전체 조회");
        userService.deleteUser(userJackson);
        List<User> getAllUser2 = userService.getAllUserList();
        userService.printUserListInfo(getAllUser2);
        System.out.println();


        System.out.println("=== [Channel] ===");
        System.out.println("채널 생성");
        Channel studyChannel = channelService.createChannel("Study", userBob);
        Channel bookChannel = channelService.createChannel("Book", userBob);
        System.out.println();

        System.out.println("전체 조회");
        List<Channel> channelList = channelService.getAllChannelList();
        channelService.printChannelListInfo(channelList);
        System.out.println();

        System.out.println("단일 조회");
        Channel searchChannel = channelService.searchById(studyChannel.getId());
        channelService.printChannelInfo(searchChannel);
        System.out.println();

        System.out.println("채널 수정 후 확인");
        channelService.updateTitle(studyChannel, "Running");
        channelService.printChannelInfo(studyChannel);
        System.out.println();

        System.out.println("채널 삭제 후 확인");
        channelService.deleteChannel(bookChannel);
        List<Channel> channelList2 = channelService.getAllChannelList();
        channelService.printChannelListInfo(channelList2);
        System.out.println();


        System.out.println("=== [Message] ===");
        System.out.println("메세지 생성");
        Message message = messageService.createMessage(studyChannel, userBob, "gooood!");
        Message message2 = messageService.createMessage(studyChannel, userBob, "yes!");
        Message message3 = messageService.createMessage(bookChannel, userBob, "run");
        System.out.println();

        System.out.println("전체 조회");
        List<Message> messageList = messageService.getAllMessageList();
        messageService.printMessageListInfo(messageList);
        System.out.println();

        System.out.println("단건 조회");
        Message searchMessage = messageService.searchById(message.getId());
        messageService.printMessageInfo(searchMessage);
        System.out.println();

        System.out.println("메세지 내용 수정 후 확인");
        messageService.updateMessage(message2, "no!");
        messageService.printMessageInfo(message2);
        System.out.println();

        System.out.println("메세지 삭제 후 확인");
        messageService.deleteMessage(message);
        List<Message> messageList2 = messageService.getAllMessageList();
        messageService.printMessageListInfo(messageList2);
        System.out.println();

    }
}
