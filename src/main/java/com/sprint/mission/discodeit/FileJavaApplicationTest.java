package com.sprint.mission.discodeit;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.file.FileChannelService;
import com.sprint.mission.service.file.FileMessageService;
import com.sprint.mission.service.file.FileUserService;

public class FileJavaApplicationTest {

    public static void main(String[] args) {

        // 채널 CRUD 구현
        FileUserService userService = new FileUserService();
        FileChannelService channelService = new FileChannelService();
        FileMessageService messageService = new FileMessageService();

        // 데이터 초기화
        userService.resetData();
        channelService.resetData();
        messageService.resetData();


        // Jack, Bob 계정 생성
        User jack = userService.createUser("codeit@codeit.com", "Jack");
        User bob = userService.createUser("yeoksam2@codeit.com", "Bob");

        // 전체 유저 조회
        userService.findAllUserList();

        // Jack의 이메일 변경
        userService.updateMail(jack, "sprint@codeit.com");
        userService.findAllUserList();


        // Bob 계정 삭제
        userService.deleteUser(bob);
        userService.findAllUserList();

        // Jack, Bob의 채널 생성 -> 각 유저마다 채널 객체 생성
        channelService.createChannel(jack, "codeit");
        channelService.createChannel(bob, "codeit2");
        channelService.createChannel(jack, "codeit3");


        // 전체 채널 조회
        channelService.findAllChannelList();

        // 채널명 변경
        channelService.updateChannel(jack,"codeit3", "sprint");
        channelService.findAllChannelList();

        // 채널 삭제
        channelService.deleteChannel("sprint");
        channelService.findAllChannelList();

        // 메시지 생성 -> 채널에 메시지 객체 생성
        Channel jackChannel = channelService.createChannel(jack, "codeit");
        Channel bobChannel = channelService.createChannel(bob, "codeit2");
        messageService.createMessage(jack, jackChannel, "수정 전 메시지 생성1");
        messageService.createMessage(bob, bobChannel, "수정 전 메시지 생성2");


        // 특정 유저 메시지 조회
        messageService.printChannelMessage(bob);

        //메시지 변경
        messageService.updateMessage(bob, bobChannel, "수정 전 메시지 생성1", "Bob입니다.");
        messageService.findAllMessageList();

        // 메시지 삭제
        messageService.deleteMessage("수정 전 메시지 생성2");
        messageService.findAllMessageList();
    }
}
