package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService,channelService);

        //User 등록
        System.out.println("User 생성");
        User user1 = new User("Alice","alice@email.com","pwd1","01011112222","서울");
        User user2 = new User("Bob","bob@email.com","pwd2","01033334444","경기도");
        User user3 = new User("Chris","chris@email.com","pwd3","01055556666","강원도");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        //User 단일 조회
        System.out.println("\nUser 단일 조회");
        UUID user1Id = user1.getId();
        User readUser = userService.read(user1Id);
        System.out.println(user1.getUsername()+": "+readUser.toString());

        //User 모두 조회
        System.out.println("\nUser 모두 조회");
        userService.readAll().forEach(users-> System.out.println(users.toString()));

        //User 수정
        System.out.println("\nUser1 주소 수정");
        user1.updateAddress("부산");
        userService.update(user1.getId(),user1);
        User updateUser = userService.read(user1Id);
        System.out.println("User1 주소 수정 후");
        userService.readAll().forEach(users -> System.out.println(users.toStringUpdate()));

        //User 삭제
        System.out.println("\nUser1 삭제");
        userService.delete(user1.getId());
        System.out.println("\nUser1 삭제후 모두 조회");
        userService.readAll().forEach(users-> System.out.println(users.toString()));

        System.out.println("\n///////////////////////////////////////////////////////\n");

        //채널 생성
        System.out.println("Channel 생성");
        Channel channel1 = new Channel("자기소개 채널","자기소개","자기소개 해주세요","운영진1");
        Channel channel2 = new Channel("스터디 채널","스터티","스터디 모집글","스프린터1");
        Channel channel3 = new Channel("자유 채널","자유대화","자유롭게 대화하세요","운영진2");

        channelService.create(channel1);
        channelService.create(channel2);
        channelService.create(channel3);

        //채널 단일 조회
        System.out.println("\nChannel 단일 조회");
        UUID channel1Id = channel1.getId();
        Channel readChannel = channelService.read(channel1Id);
        System.out.println(channel1.getChannelName()+": "+readChannel.toString());

        //채널 모두 조회
        System.out.println("\nChannel 모두 조회");
        channelService.readAll().forEach(channel -> System.out.println(channel.toString()));

        //채널 수정
        System.out.println("\nChannel 수정");
        channel2.updateChannelName("운동 채널");
        channel2.updateNotificationTitle("운동 그룹 모집");
        channel2.updateNotificationContent("운동 그룹 모집합니다.");
        UUID channel2Id = channel2.getId();
        channelService.update(channel2Id,channel2);
        System.out.println("Channel2 채널명,제목,내용 수정 후");
        channelService.readAll().forEach(channel -> System.out.println(channel.toStringUpdate()));

        //채널 삭제
        System.out.println("\nChannel 삭제");
        channelService.delete(channel1Id);
        System.out.println("Channel1 삭제 후 모두 조회");
        channelService.readAll().forEach(channel -> System.out.println(channel.toString()));

        System.out.println("\n///////////////////////////////////////////////////////\n");

        //메세지 생성
        System.out.println("Message 생성");
        Message msg1 = new Message("안녕하세요",user3,channel2,"^^");
        Message msg2 = new Message("화이팅!",user2,channel2,"★");

        messageService.create(msg1);
        messageService.create(msg2);

        //메세지 단일 조회
        System.out.println("\nMessage 단일 조회");
        UUID message1Id = msg1.getSender().getId();
        Message readMessage = messageService.read(message1Id);
        System.out.println(readMessage.toString());

        //메시지 모두 조회
        System.out.println("\nMessage 모두 조회");
        messageService.readAll().forEach(message -> System.out.println(message.toString()));

        //메시지 수정
        System.out.println("\nMessage1 이모티콘 수정");
        msg1.updateEmoji("♡");
        messageService.update(message1Id,msg1);
        System.out.println("\nMessage 수정 후");
        messageService.readAll().forEach(message -> System.out.println(message.toStringUpdate()));


        //메시지 삭제
        System.out.println("\nMessage 삭제");
        messageService.delete(message1Id);
        System.out.println("\nMessage1 삭제 후 모두 조회");
        messageService.readAll().forEach(message -> System.out.println(message.toString()));
    }
}