package com.sprint.mission;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.domain.BinaryContentService;
import com.sprint.mission.discodeit.service.domain.ReadStatusService;
import com.sprint.mission.discodeit.service.domain.UserStatusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//@SpringBootApplication
public class DiscodeitApplication {

    /*public static void main(String[] args) throws InterruptedException {
        // 서비스 초기화
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        AuthService authService = context.getBean(AuthService.class);
        // 셋업
        //UserDto userDto = setupUser(userService);
        //ChannelDto channel = setupChannel(channelService);

        // 테스트
        //messageCreateTest(messageService, channelDto, userDto);

        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\channel.ser");
        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\message.ser");
        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\users.ser");
        resetMessageFile("C:\\Users\\ypd06\\codit\\files\\BinaryContent.ser");

        UserDto userDto1 = userService.createUser(new UserDto("Yang", "YangPassword", "yang@naver.com"));
        UserDto userDto2 = userService.createUser(new UserDto("Kim", "KimPassword", "kim@naver.com"));
        UserDto userDto3 = userService.createUser(new UserDto("Lee", "LeePassword", "lee@naver.com"));
        UserDto userDto4 = userService.createUser(new UserDto("Han", "HanPassword", "han@naver.com"));
        UUID user1 = userDto1.id();
        UUID user2 = userDto2.id();
        UUID user3 = userDto3.id();
        UUID user4 = userDto4.id();

        ChannelDto channelDto1 = channelService.create(new ChannelDto("SBS", ChannelType.PUBLIC));
        ChannelDto channelDto2 = channelService.create(new ChannelDto("KBS", ChannelType.PUBLIC));
        ChannelDto channelDto3 = channelService.create(new ChannelDto("MBC", ChannelType.PUBLIC));
        UUID channel1 = channelDto1.id();
        UUID channel2 = channelDto2.id();
        UUID channel3 = channelDto3.id();
        System.out.println("====================================================");
        System.out.println("로그인 로직 확인");
        UserDto authenticatedUser = authService.login("Yang", "YangPassword");
        System.out.println("authenticatedUser = " + authenticatedUser);
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
        userService.updateUser(new UserDto(user1, "YangPassword", "Park", "Park@daum.net")); //Password 추후 어떻게 처리할 지 고민해봐야할 듯
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
        System.out.println(channelService.find(channel1));
        System.out.println("====================================================");
        System.out.println("채널 다건 조회");
        ChannelFindAllDto channels = channelService.findAll();
        channels.channels().forEach(System.out::println);
        channels.latestMessagesInstant().values().forEach(System.out::println);
        channels.userList().forEach(System.out::println);
        System.out.println("====================================================");
        System.out.println("====================================================");
        System.out.println("채널 수정");
        channelService.updateChannel(new ChannelDto(channel2, "EBS"));
        System.out.println(channelService.find(channel2));
        System.out.println("====================================================");
        System.out.println("채널 메시지 등록");
        UUID message1 = channelService.addMessageInChannel(new MessageDto(user4, channel1, "hello"));
        System.out.println(message1 + " 등록완료");
        UUID message2 = channelService.addMessageInChannel(new MessageDto(user3, channel1, "world"));
        System.out.println(message2 + " 등록완료");
        UUID message3 = channelService.addMessageInChannel(new MessageDto(user1, channel1, "super"));
        System.out.println(message3 + " 등록완료");
        UUID message4 = channelService.addMessageInChannel(new MessageDto(user3, channel2, "mario"));
        System.out.println(message4 + " 등록완료");
        System.out.println("====================================================");
        System.out.println("채널 메시지 다건 출력 ");
        List<UUID> messages = channelService.findAllMessagesByChannelId(channel1);
        for (UUID message : messages) {
            System.out.println(messageService.findById(message));
        }
        System.out.println("====================================================");
        System.out.println("하나의 채널 메시지 삭제 : Channel 1 의 Message1 삭제 ");
        messageService.delete(message1);
        messages = channelService.findAllMessagesByChannelId(channel1);
        for (UUID message : messages) {
            System.out.println(messageService.findById(message));
        }
        System.out.println("====================================================");
        System.out.println("채널 삭제 시 메시지 처리");
        channelService.deleteChannel(channel1);
        try{
            //채널 찾기
            ChannelFindDto channelFindDto = channelService.find(channel1);
            System.out.println("channelFindDto = " + channelFindDto);
        } catch (IllegalStateException e) {
            System.out.println("channelService.find : " + e.getMessage());
        }
        try {
           //채널 속 메시지 찾기
            messageService.findAllByChannelId(channel1).forEach(System.out::println);
        } catch (IllegalStateException e) {
            System.out.println("channelService.findAllMessagesByChannelId : " + e.getMessage());
        }

        System.out.println("====================================================");
        System.out.println("ReadStatus 로직 확인 - PRIVATE 채널만 ReadStatus 생성");
        ChannelDto channelDto4 = channelService.create(new ChannelDto("KBS2", ChannelType.PRIVATE), Arrays.asList(user1, user2, user3));
        UUID channel4 = channelDto4.id();
        List<ReadStatusDto> readStatuses = readStatusService.findAllByChannelId(channel4);
        readStatuses.forEach(System.out::println);
        System.out.println("====================================================");
        System.out.println("findById");
        ReadStatusDto readStatusDto = readStatusService.findById(readStatuses.get(0).id());// channel4
        System.out.println("findById readStatusDto = " + readStatusDto);
        System.out.println("====================================================");
        System.out.println("findAllByUserId : 한 유저가 여러 채널에 가입해 있는 경우");
        ChannelDto channelDto5 = channelService.create(new ChannelDto("EBS2", ChannelType.PRIVATE), Arrays.asList(user1, user2, user3));
        UUID channel5 = channelDto5.id();
        List<ReadStatusDto> readStatusByUserId = readStatusService.findAllByUserId(user3);
        System.out.println("readStatusByUserId = " + readStatusByUserId);
        System.out.println("readStatusByUserId.size() = " + readStatusByUserId.size());
        System.out.println("====================================================");
        System.out.println("update readStatus : 위에서 찾은 두개의 ReadStatus에서 두 번째 값을 첫 번째 값에 덮어씀");
        readStatusService.update(readStatusByUserId.get(0), readStatusByUserId.get(1));
        readStatusByUserId = readStatusService.findAllByUserId(user3);
        System.out.println("update readStatus : 메시지를 유저 아이디를 통해 찾을 떄 UserStatus 업데이트 ");
        messageService.findAllByUserId(user3).forEach(System.out::println);
        System.out.println("====================================================");
        System.out.println("두 값의 ID 값은 변하지 않고 나머지는 바뀔 수 있는데 위 예시는 ChannelId가 가장 먼저 확인할 수 있다.");
        System.out.println("readStatusByUserId = " + readStatusByUserId);
        System.out.println("readStatusByUserId.size() = " + readStatusByUserId.size());
        System.out.println("====================================================");
        System.out.println("Delete ReadStatus");
        readStatusService.delete(readStatusByUserId.get(0).id());
        readStatusByUserId = readStatusService.findAllByUserId(user3);
        System.out.println("readStatusByUserId = " + readStatusByUserId);
        System.out.println("readStatusByUserId.size() = " + readStatusByUserId.size());
        System.out.println("====================================================");
        System.out.println("UserStatus 로직 확인");
        System.out.println("====================================================");
        System.out.println("Find All UserStatus");
        System.out.println("Instant.now() = " + Instant.now());
        Thread.sleep(1000); //1초만 지나도 모두 OFFLINE이 되도록 설정하기 위한 지연
        List<UserStatus> userStatuses = userStatusService.findAll();
        userStatuses.forEach(System.out::println);
        System.out.println("====================================================");
        System.out.println("Find By Id");
        UserStatus userStatusById = userStatusService.findById(userStatuses.get(0).getId());
        System.out.println("userStatusById = " + userStatusById);
        System.out.println("====================================================");
        System.out.println("Find By UserId : user1");
        UserStatus userStatusByUserId = userStatusService.findByUserId(user1);
        System.out.println("userStatusByUserId = " + userStatusByUserId);
        System.out.println("====================================================");
        System.out.println("update userStatus : ONLINE 여부 확인");
        UserStatusDto updateUserStatus = new UserStatusDto(userStatusByUserId.getUserId(), userStatusByUserId.getUserId(), userStatusByUserId.getCreatedAt(), Instant.now(), null);
        userStatusService.update(new UserStatusDto(userStatusByUserId), updateUserStatus);
        userStatusByUserId = userStatusService.findByUserId(user1);
        System.out.println("userStatusByUserId = " + userStatusByUserId);
        System.out.println("====================================================");
        System.out.println("Delete UserStatus");
        userStatusService.delete(userStatusByUserId.getId());
        userStatusByUserId = userStatusService.findByUserId(user1);
        System.out.println("userStatusByUserId = " + userStatusByUserId);
        System.out.println("===================================================");
        System.out.println("BinaryContent 로직 확인 : 유저");
        File file = new File("C:\\Users\\ypd06\\codit\\files\\BinaryContentTest.txt");
        UserDto userDto5 = userService.createUser(new UserDto("Ha", "HaPassword", "ha@naver.com", new BinaryContentDto(file)));
        binaryContentService.findAll().forEach(System.out::println);
        System.out.println("===================================================");
        System.out.println("find By DomainId : 유저");
        List<BinaryContentDto> binaryContentByDomainId = binaryContentService.findByDomainId(userDto5.id());
        System.out.println("binaryContentByDomainId = " + binaryContentByDomainId);
        System.out.println("===================================================");
        System.out.println("find By Id : 유저");
        System.out.println("find By Id : " +binaryContentService.findById(binaryContentByDomainId.get(0).id()));
        System.out.println("===================================================");
        System.out.println("delete By id : 유저 : 삭제 후 모든 BinaryContent 출력");
        binaryContentService.delete(binaryContentByDomainId.get(0).id());
        System.out.println("binaryContentService.findAll().isEmpty() : " + binaryContentService.findAll().isEmpty());


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
    }*/
}

