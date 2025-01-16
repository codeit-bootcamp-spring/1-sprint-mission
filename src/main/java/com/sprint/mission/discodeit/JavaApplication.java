package com.sprint.mission.discodeit;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.jcf.JCFChannelService;
import com.sprint.mission.jcf.JCFMessageService;
import com.sprint.mission.jcf.JCFUserService;
import com.sprint.mission.service.ChannelService;


public class JavaApplication {

    public static void main(String[] args) {


        // 계정 CRUD 구현
        JCFUserService userService = JCFUserService.getInstance();

        // Jack, Bob 계정 생성 -> 각 유저마다 유저 객체 생성
        User Jack = userService.createUser("codeit@codeit.com", "Jack");
        User Bob = userService.createUser("yeoksam2@codeit.com", "Bob");

        // 전체 유저 조회
        userService.getSearchAllUser();

        // Jack, Bob 이메일 변경
        userService.updateMail(Jack, "sprint@codeit.com");
        userService.updateMail(Bob, "yeoksamWework2@codeit.com");
        userService.getSearchAllUser();

        // Jack 계정 삭제
        userService.deleteUser(Jack);
        userService.getSearchAllUser();

        // 채널 CRUD 구현
        ChannelService channelService = JCFChannelService.getInstance();

        // Jack, Bob의 채널 생성 -> 각 유저마다 채널 객체 생성
        channelService.createChannel(Jack, "codeit");
        channelService.createChannel(Bob, "codeit2");
        channelService.createChannel(Jack, "codeit3");

        // 전체 채널 조회
        channelService.getAllChannelList();

        // 채널명 변경
        channelService.updateChannel(Jack,"codeit3", "sprint");
        channelService.getAllChannelList();

         // 채널 삭제
        channelService.deleteChannel("sprint");
        channelService.getAllChannelList();

        // 메시지 CRUD 구현
        JCFMessageService messageService = JCFMessageService.getInstance();

        // 메시지 생성 -> 채널에 메시지 객체 생성
        Channel channel = channelService.createChannel(Bob, "codeit2");
        messageService.createMessage(Bob, channel, "수정 전 메시지 생성1");
        messageService.createMessage(Bob, channel, "수정 전 메시지 생성2");

        // 특정 유저 메시지 조회
        messageService.printChannelMessage(Bob);

        //메시지 변경
        messageService.updateMessage(Bob, channel, "수정 전 메시지 생성1", "Bob입니다.");
        messageService.getAllMessageList();

        // 메시지 삭제
        messageService.deleteMessage("수정 전 메시지 생성2");
        messageService.getAllMessageList();
    }
}
