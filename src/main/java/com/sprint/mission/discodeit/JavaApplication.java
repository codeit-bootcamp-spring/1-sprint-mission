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
        ChannelService channelService = new JCFChannelService(userService);
        MessageService messageService = new JCFMessageService(userService, channelService);

        System.out.println("=== [User] ===");
        System.out.println("유저 생성");
        User userBob = userService.createUser("Bob", "qwe123@google.com");
        User userJackson = userService.createUser("Jackson", "Jackson@google.com");
        System.out.println();

        System.out.println("전체 조회");
        List<User> getAllUser = userService.getAllUserList();
        for (User user : getAllUser) {
            System.out.println(user);
        }
        System.out.println();

        System.out.println("단건 조회");
        User searchUser = userService.searchById(userBob.getId());
        System.out.println(searchUser);
        System.out.println();

        System.out.println("유저 이메일 수정 후 확인");
        userService.updateUserEmail(userBob.getId(), "bob@gmail.com");
        System.out.println(userBob);
        System.out.println();

        System.out.println("유저 삭제 후 전체 조회");
        userService.deleteUser(userJackson.getId());
        List<User> getAllUser2 = userService.getAllUserList();
        for (User user : getAllUser2) {
            System.out.println(user);
        }
        System.out.println();


        System.out.println("=== [Channel] ===");
        System.out.println("채널 생성");
        Channel studyChannel = channelService.createChannel("Study","Study room",userBob.getId());
        Channel bookChannel = channelService.createChannel("Book","book room", userBob.getId());
        System.out.println();

        System.out.println("전체 조회");
        List<Channel> channelList = channelService.getAllChannelList();
        for (Channel channel : channelList) {
            System.out.println(channel);
        }
        System.out.println();

        System.out.println("단일 조회");
        Channel searchChannel = channelService.searchById(studyChannel.getId());
        System.out.println(searchChannel);
        System.out.println();

        System.out.println("채널 수정 후 확인");
        channelService.updateTitle(studyChannel.getId(), "Running");
        System.out.println(studyChannel);
        System.out.println();

        System.out.println("채널 삭제 후 확인");
        channelService.deleteChannel(bookChannel.getId());
        List<Channel> channelList2 = channelService.getAllChannelList();
        for (Channel channel : channelList2) {
            System.out.println(channel);
        }
        System.out.println();


        System.out.println("=== [Message] ===");
        System.out.println("메세지 생성");
        Message message1 = messageService.createMessage(studyChannel.getId(), userBob.getId(), "gooood!");
        Message message2 = messageService.createMessage(studyChannel.getId(), userBob.getId(), "yes!");
        Message message3 = messageService.createMessage(bookChannel.getId(), userBob.getId(), "run");
        System.out.println();

        System.out.println("전체 조회");
        List<Message> messageList = messageService.getAllMessageList();
        for (Message message : messageList) {
            System.out.println(message);
        }
        System.out.println();

        System.out.println("단건 조회");
        Message searchMessage = messageService.searchById(message1.getId());
        System.out.println(message2);
        System.out.println();

        System.out.println("메세지 내용 수정 후 확인");
        messageService.updateMessage(message2.getId(), "no!");
        System.out.println(message2);
        System.out.println();

        System.out.println("메세지 삭제 후 확인");
        messageService.deleteMessage(message1.getId());
        List<Message> messageList2 = messageService.getAllMessageList();
        for (Message message : messageList2) {
            System.out.println(message);
        }
        System.out.println();

    }
}
