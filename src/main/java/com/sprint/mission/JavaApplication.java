package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.factory.JCFServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = JCFServiceFactory.getInstance().getUserService();
        ChannelService channelService = JCFServiceFactory.getInstance().getChannelService();
        MessageService messageService = JCFServiceFactory.getInstance().getMessageService();


        // User test
        // user 생성
        System.out.println("======= User ========");
        System.out.println("유저 생성");

        User user1 = userService.createUser("test1", "nickname1", "email1",
                "password1", "profileImageUrl1");
        User user2 = userService.createUser("test2", "nickname2", "email2",
                "password2", "profileImageUrl2");
        User user3 = userService.createUser("test3", "nickname3", "email3",
                "password3", "profileImageUrl3");
        User user4 = userService.createUser("test4", "nickname4", "email4",
                "password4", "profileImageUrl4");

        System.out.println(user1.getName());
        System.out.println();

        // user 조회
        System.out.println("유저 조회");

        User findUser1 = userService.findUserById(user1.getId());
        UUID findUser1Id = findUser1.getId();

        System.out.println(findUser1.getName());
        System.out.println();

        // user 목록 조회
        System.out.println("유저 목록 조회");

        List<User> userList = userService.findAllUsers();

        userList.forEach(user -> System.out.println(user.getName()));
        System.out.println();

        // user 업데이트
        System.out.println("유저 업데이트");
        System.out.println("변경 전");
        System.out.println(findUser1.getName());

        userService.updateUser(findUser1Id,"updateTest1", "nickname1", "email1",
                "password1", "profileImageUrl1");
        User updateUser1 = userService.findUserById(findUser1Id);

        System.out.println("변경 후");
        System.out.println(updateUser1.getName());
        System.out.println();

        // user 삭제
        System.out.println("유저 삭제");

        userService.deleteUser(findUser1Id);
        try {
            userService.findUserById(findUser1Id);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println();


        // Channel test
        // 채널 생성
        System.out.println("======= Channel ========");
        System.out.println("채널 생성");

        UUID channelOwnerId = user2.getId();
        Channel channel1 = channelService.createChannel(channelOwnerId, "channel1");
        Channel channel2 = channelService.createChannel(channelOwnerId, "channel2");

        System.out.println(channel1.getName());
        System.out.println();

        // channel 조회
        System.out.println("채널 조회");

        Channel findChannel1 = channelService.findChannelById(channel1.getId());
        UUID findChannel1Id = findChannel1.getId();

        System.out.println(findChannel1.getName());
        System.out.println();

        // 채널 목록 조회
        System.out.println("채널 목록 조회");

        List<Channel> channelList = channelService.findAllChannels();

        channelList.forEach(channel -> System.out.println(channel.getName()));
        System.out.println();

        // 채널 인원 초대
        System.out.println("채널 인원 초대");

        List<User> channelUsers = new ArrayList<>();
        channelUsers.add(user3);
        channelUsers.add(user4);
        channelService.inviteUsers(findChannel1Id, channelUsers);

        findChannel1.getChannelUserList().forEach(user -> System.out.println(user.getName()));
        System.out.println("채널 인원 수");
        System.out.println(findChannel1.getChannelUserList().size());
        System.out.println();

        // 채널 나가기
        System.out.println("채널 나가기");

        channelService.leaveUsers(findChannel1Id, channelUsers);

        findChannel1.getChannelUserList().forEach(user -> System.out.println(user.getName()));
        System.out.println("채널 인원 수");
        System.out.println(findChannel1.getChannelUserList().size());
        System.out.println();

        // 채널명 변경
        System.out.println("채널명 변경");
        System.out.println("변경 전");
        System.out.println(findChannel1.getName());

        channelService.updateChannelName(channelOwnerId, findChannel1Id, "updateChannel1");

        System.out.println("변경 후");
        System.out.println(findChannel1.getName());

        // 채널 삭제
        channelService.deleteChannel(channelOwnerId, findChannel1Id);
        try {
            channelService.findChannelById(findChannel1Id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println();

        // Message test
        // 메세지 생성
        System.out.println("======= Message ========");
        System.out.println("메세지 생성");

        UUID sendUserId = user3.getId();
        UUID channelId = channel2.getId();
        Message message1 = messageService.createMessage(sendUserId, channelId, "hello");
        Message message2 = messageService.createMessage(sendUserId, channelId, "world");

        System.out.println(message1.getContent());
        System.out.println(message2.getContent());
        System.out.println();

        // 메세지 조회
        System.out.println("메세지 조회");

        Message findMessage1 = messageService.findMessageById(message1.getId());

        System.out.println(findMessage1.getContent());
        System.out.println();

        // 메세지 목록 조회
        System.out.println("메세지 목록 조회");

        List<Message> findMessagList = messageService.findAllMessage();

        findMessagList.forEach(message -> System.out.println(message.getContent()));
        System.out.println();

        // 메세지 수정
        System.out.println("메세지 수정");
        System.out.println("변경 전");
        System.out.println(findMessage1.getContent());

        UUID findMessage1Id = findMessage1.getId();
        messageService.updateMessage(sendUserId, findMessage1Id, "hi");

        System.out.println("변경 후");
        System.out.println(findMessage1.getContent());
        System.out.println();

        // 메세지 삭제
        System.out.println("메세지 삭제");

        messageService.deleteMessage(sendUserId, findMessage1Id);
        try {
            messageService.findMessageById(findMessage1Id);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}