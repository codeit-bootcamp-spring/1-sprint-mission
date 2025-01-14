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
        System.out.println("계정생성: " + userA.getUsername());

        User userB = userService.createUser("Suyeon", "suyeon@naver.com");
        System.out.println("계정생성: " + userB.getUsername());

        User retrievedUser = userService.getUser(userA.getId());
        System.out.println("조회된 유저: " + retrievedUser.getUsername());

        List<User> allUsers = userService.getAllUsers();
        System.out.println("전체계정: " + allUsers.size());

        Channel channelA = channelService.createChannel("일반", "일반채널");
        System.out.println("채널생성: " + channelA.getName());

        Channel channelB = channelService.createChannel("공지사항", "공지사항 채널");
        System.out.println("채널생성: " + channelB.getName());

        Message messageA = messageService.createMessage(userA.getId(), channelA.getId(), "안녕하세요");
        System.out.println("A채널에 메세지 생성: " + messageA.getContent());

        Message messageB = messageService.createMessage(userB.getId(), channelB.getId(), "공지사항입니다");
        System.out.println("B채널에 메세지 생성: " + messageB.getContent());

        User updatedUser = userService.updateUser(userA.getId(), "Junhan", "jun5231@naver.com");
        System.out.println("업데이트  유저: " + updatedUser.getUsername());

        User retrievedUpdatedUser = userService.getUser(userA.getId());
        System.out.println("업데이트된 유저 조회: " + retrievedUpdatedUser.getUsername());

        messageService.deleteMessage(messageA.getId());
        System.out.println("삭제 메세지: " + messageA.getContent());

        if (messageService.getMessage(messageA.getId()) == null) {
            System.out.println("메시지가 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("메시지 삭제에 실패했습니다.");
        }

        List<Channel> allChannels = channelService.getAllChannels();
        System.out.println("전체채널: " + allChannels.size());
    }
}
