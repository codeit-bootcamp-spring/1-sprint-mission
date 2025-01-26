package com.sprint.mission;

import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // 싱글톤 객체 생성
        /*
        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        //유저 생성
        UUID user1 = userService.createUser("Kaya");
        UUID user2 = userService.createUser("Ayden");
        UUID user3 = userService.createUser("Sia");

        //채널 생성
        UUID channel1 = channelService.createChannel("Kaya's Channel");
        //채널1에 모든 유저 추가
        userService.getUsersMap().keySet().forEach(user -> channelService.addChannelMember(channel1, user));
        //채널1에 속한 유저 이름 출력
        channelService.getChannel(channel1).getMembers().forEach(
                member -> System.out.println(channelService.getChannel(channel1).getChannelName()+" 채널의 멤버 "+ member.getUserName())
        );
        //메세지 생성
        UUID message1 = messageService.sendMessage(user1, channel1, "Hello World");
        //메세지 수정
        messageService.reviseMessage(message1, "Bye World");
        //메세지 내용 출력
        System.out.println("메세지 내용:" + messageService.getMessageContent(message1));
        //메세지 삭제
        messageService.deleteMessage(message1);
        //메세지 존재 시 내용 출력. 존재하지 않을 시 메세지 출력.
        if (messageService.isMessageExist(message1) == false) {
            System.out.println("해당 메세지가 존재하지 않습니다.");
        } else {
            System.out.println(messageService.getMessageContent(message1));
        }*/
    }
}