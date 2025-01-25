package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        User userA = userService.createUser("Jun", "junhan5231@naver.com");
        System.out.println("유저 생성: " + userA.getUsername());

        User retrievedUser = userService.getUser(userA.getId());
        System.out.println("조회된 유저: " + retrievedUser.getUsername());

        List<User> allUsers = userService.getAllUsers();
        System.out.println("전체 유저 수: " + allUsers.size());

        User updatedUser = userService.updateUser(userA.getId(), "Junhan", "jun5231@naver.com");
        System.out.println("수정된 유저: " + updatedUser.getUsername());

        User retrievedUpdatedUser = userService.getUser(userA.getId());
        System.out.println("수정 후 조회된 유저: " + retrievedUpdatedUser.getUsername());

        Channel channelA = channelService.createChannel("일반", "일반채널");
        System.out.println("채널 생성: " + channelA.getName());

        Channel retrievedChannel = channelService.getChannel(channelA.getId());
        System.out.println("조회된 채널: " + retrievedChannel.getName());

        List<Channel> allChannels = channelService.getAllChannels();
        System.out.println("전체 채널 수: " + allChannels.size());

        Channel updatedChannel = channelService.updateChannel(channelA.getId(), "수정된 일반", "수정된 일반채널");
        System.out.println("수정된 채널: " + updatedChannel.getName());

        Channel retrievedUpdatedChannel = channelService.getChannel(channelA.getId());
        System.out.println("수정 후 조회된 채널: " + retrievedUpdatedChannel.getName());

        Message messageA = messageService.createMessage(userA.getId(), channelA.getId(), "안녕하세요");
        System.out.println("메시지 생성: " + messageA.getContent());

        Message retrievedMessage = messageService.getMessage(messageA.getId());
        System.out.println("조회된 메시지: " + retrievedMessage.getContent());

        List<Message> allMessages = messageService.getAllMessages();
        System.out.println("전체 메시지 수: " + allMessages.size());

        Message updatedMessage = messageService.updateMessage(messageA.getId(), "수정된 메시지입니다");
        System.out.println("수정된 메시지: " + updatedMessage.getContent());

        Message retrievedUpdatedMessage = messageService.getMessage(messageA.getId());
        System.out.println("수정 후 조회된 메시지: " + retrievedUpdatedMessage.getContent());

        messageService.deleteMessage(messageA.getId());
        if (messageService.getMessage(messageA.getId()) == null) {
            System.out.println("메시지가 성공적으로 삭제되었습니다.");
        }

        channelService.deleteChannel(channelA.getId());
        if (channelService.getChannel(channelA.getId()) == null) {
            System.out.println("채널이 성공적으로 삭제되었습니다.");
        }

        userService.deleteUser(userA.getId());
        if (userService.getUser(userA.getId()) == null) {
            System.out.println("유저가 성공적으로 삭제되었습니다.");
        }
    }
}
