package com.sprint.mission;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.factory.Factory;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class JcfServicesTestMain {
    public static void main(String[] args) {
        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\channel.ser");
        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\message.ser");
        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\users.ser");
        //기존 JCF 내용입니다.
        Factory factory = new Factory("jcf");
        JcfUserService userService = factory.getJcfUserService();
        JcfChannelService channelService = factory.getJcfchannelService();
        JcfMessageService messageService = factory.getJcfMessageService();

        UserDto userDto1 = userService.createUser(new UserDto("Yang", "yang@naver.com"));
        UserDto userDto2 = userService.createUser(new UserDto("Kim", "kim@naver.com"));
        UserDto userDto3 = userService.createUser(new UserDto("Lee", "lee@naver.com"));
        UserDto userDto4 = userService.createUser(new UserDto("Han", "han@naver.com"));
        UUID user1 = userDto1.id();
        UUID user2 = userDto2.id();
        UUID user3 = userDto3.id();
        UUID user4 = userDto4.id();

        UUID channel1 = channelService.createChannel("SBS");
        UUID channel2 = channelService.createChannel("KBS");
        UUID channel3 = channelService.createChannel("MBC");
        System.out.println("====================================================");
        System.out.println("유저 단건 조회");
        System.out.println(userService.getUser(user1));
        System.out.println("====================================================");
        System.out.println("유저 다건 조회");
        List<UserDto> users = userService.getUsers();
        for (UserDto user : users) {
            System.out.println(user);
        }
        System.out.println("====================================================");
        System.out.println("유저 수정");
        userService.updateUser(new UserDto(user1, "Park", "Park@daum.net"));
        System.out.println(userService.getUser(user1));
        System.out.println("====================================================");
        System.out.println("유저 삭제");
        userService.deleteUser(user2);
        users = userService.getUsers();
        for (UserDto user : users) {
            System.out.println(user);
        }

        System.out.println("====================================================");
        System.out.println("채널 단건 조회");
        System.out.println(channelService.getChannel(channel1));
        System.out.println("====================================================");
        System.out.println("채널 다건 조회");
        List<Channel> channels = channelService.getChannels();
        for (Channel channel : channels) {
            System.out.println(channel);
        }
        System.out.println("====================================================");
        System.out.println("채널 수정");
        channelService.updateChannel(channel2, "EBS");
        System.out.println(channelService.getChannel(channel2));
        System.out.println("====================================================");
        System.out.println("채널 메시지 등록");
        UUID message1 = channelService.addMessage_By_Channel(channel1, user4, "hello");
        System.out.println(message1 + " 등록완료");
        UUID message2 = channelService.addMessage_By_Channel(channel1, user3, "world");
        System.out.println(message2 + " 등록완료");
        UUID message3 = channelService.addMessage_By_Channel(channel1, user1, "super");
        System.out.println(message3 + " 등록완료");
        UUID message4 = channelService.addMessage_By_Channel(channel2, user3, "mario");
        System.out.println(message4 + " 등록완료");
        System.out.println("====================================================");

        System.out.println("채널 메시지 다건 출력 ");
        List<Message> messages = channelService.messages(channel1);
        for (Message message : messages) {
            System.out.println(message);
        }
        System.out.println("====================================================");

        System.out.println("채널 메시지 삭제");
        messageService.deleteMessage(message1);
        messages = channelService.messages(channel1);
        for (Message message : messages) {
            System.out.println(message);
        }
        System.out.println("====================================================");
        System.out.println("채널 삭제 시 메시지 처리");
        channelService.deleteChannel(channel1);
        messages = channelService.messages(channel1);
        System.out.println("channel1's messages = " + messages);
        System.out.println("====================================================");
        System.out.println("메시지 내용 변경");
        System.out.println("메지시 변경 이전 -> "+ messageService.getMessage(message4));
        messageService.getMessage(message4).update("Luigi");
        System.out.println("메지시 변경 이후 -> "+messageService.getMessage(message4));
        System.out.println("====================================================");
        System.out.println("유저 이름으로 메시지 확인");
        List<Message> messageByUserId = messageService.getMessagesByUserId(user3);
        System.out.println("messageByUserId = " + messageByUserId);
        System.out.println("====================================================");
        System.out.println("유저 삭제 시 메시지 삭제");
        userService.deleteUser(user3);
        messageByUserId = messageService.getMessagesByUserId(user3);
        System.out.println("messageByUserId = " + messageByUserId);
        System.out.println("====================================================");


    }
    private static void resetMessageFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            System.out.println("✅ '" + filename + "' 파일이 초기화되었습니다.");
        } catch (IOException e) {
            System.err.println("❌ 파일 초기화 중 오류 발생: " + e.getMessage());
        }
    }
}
