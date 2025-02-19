package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.dto.channel.PrivateChannelRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@SpringBootApplication
public class DiscodeitApplication {

    static void clearDataFiles() {
        // 현재 작업 디렉토리에 있는 file-data-map 경로
        Path basePath = Paths.get(System.getProperty("user.dir"), "file-data-map");

        // 삭제할 폴더 목록
        List<String> folders = List.of("User", "Channel", "Message", "ReadStatus", "UserStatus", "BinaryContent");

        for (String folder : folders) {
            Path folderPath = basePath.resolve(folder);
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                try (Stream<Path> files = Files.list(folderPath)) {
                    files
                            .filter(path -> path.toString().endsWith(".ser")) // .ser 파일만 선택
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    System.out.println("Error:" + "파일 삭제 실패: " + path + " - " + e.getMessage());
                                }
                            });
                } catch (IOException e) {
                    System.out.println("Error:" + "폴더 접근 실패: " + folderPath + " - " + e.getMessage());
                }
            }
        }
    }

    static void clearDataFiles2() {
        // 현재 작업 디렉토리에 있는 file-data-map 경로
        Path basePath = Paths.get(System.getProperty("user.dir"), "file-data-map2");

        // 삭제할 폴더 목록
        List<String> folders = List.of("User", "Channel", "Message", "ReadStatus", "UserStatus", "BinaryContent");

        for (String folder : folders) {
            Path folderPath = basePath.resolve(folder);
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                try (Stream<Path> files = Files.list(folderPath)) {
                    files
                            .filter(path -> path.toString().endsWith(".ser")) // .ser 파일만 선택
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    System.out.println("Error:" + "파일 삭제 실패: " + path + " - " + e.getMessage());
                                }
                            });
                } catch (IOException e) {
                    System.out.println("Error:" + "폴더 접근 실패: " + folderPath + " - " + e.getMessage());
                }
            }
        }
    }

    static MultipartFile convertFileToMultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);

        return new MockMultipartFile(
                "file",              // 필드명
                file.getName(),      // 원본 파일명
                "image/jpeg",        // MIME 타입 (파일 확장자에 맞게 설정 가능)
                input                // 파일 데이터
        );
    }

    //User
    static User setupUser(UserService userService, UserCreateRequest userCreateRequest) {
        try {
            User user = userService.create(userCreateRequest);
            return user;
        } catch (IllegalArgumentException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    static void readUser(UserService userService, User user) {
        try {
            UserResponse userResponse = userService.find(user.getId());
            System.out.println(userResponse);
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        }


    }

    static void readAllUsers(UserService userService) {
        List<UserResponse> list = userService.findAll();
        System.out.println("---- All Users ----");
        for (UserResponse userResponse : list) {
            System.out.println(userResponse);
        }
        System.out.println("-------------------");
    }

    static User updateUser(UserService userService, User user, UserUpdateRequest userUpdateRequest) {
        try {
            return userService.update(user.getId(), userUpdateRequest);
        } catch (IllegalArgumentException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    static void deleteUser(UserService userService, User user) {
        try {
            userService.delete(user.getId());
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Channel

    static Channel setupPublicChannel(ChannelService channelService, PublicChannelCreateRequest publicChannelCreateRequest) {
        return channelService.createChannelPublic(publicChannelCreateRequest);
    }

    static Channel setupPrivateChannel(ChannelService channelService, PrivateChannelRequest channelCreateRequest) {
        return channelService.createChannelPrivate(channelCreateRequest);
    }

    static void readChannel(ChannelService channelService, User user, Channel channel) {
        try {
            ChannelResponse channelResponse = channelService.find(user.getId(), channel.getId());
            System.out.println(channelResponse);
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error:"+e.getMessage());
        }
    }

    static void readChannelByUser(ChannelService channelService, User user) {
        List<ChannelResponse> list = channelService.findAllByUserId(user.getId());
        System.out.println("---- All Channels that the user can view ----");
        for (ChannelResponse channelResponse : list) {
            System.out.println(channelResponse);
        }
        System.out.println("---------------------------------------------");
    }

    static Channel updateChannel(ChannelService channelService, Channel channel, ChannelUpdateRequest channelUpdateRequest) {
        try {
            return channelService.update(channel.getId(), channelUpdateRequest);
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    static void deleteChannel(ChannelService channelService, Channel channel) {
        try {
            channelService.delete(channel.getId());
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Message

    static Message setupMessage(MessageService messageService, MessageCreateRequest messageCreateRequest) {
        try {
            return messageService.create(messageCreateRequest);
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        } catch (IllegalStateException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    static void readMessage(MessageService messageService, Message message) {
        try {
            System.out.println(messageService.find(message.getId()));
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    static void readAllMessages(MessageService messageService) {
        List<MessageResponse> list = messageService.findAll();
        System.out.println("---- All Messages ----");
        for (MessageResponse messageResponse : list) {
            System.out.println(messageResponse);
        }
        System.out.println("----------------------");
    }

    static void updateMessage(MessageService messageService, Message message, MessageUpdateRequest messageUpdateRequest) {
        try {
            messageService.update(message.getId(), messageUpdateRequest);
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    static void deleteMessage(MessageService messageService, Message message) {
        try {
            messageService.delete(message.getId());
        } catch (NoSuchElementException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static void main(String[] args) {

        //SpringApplication.run(DiscodeitApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        // + Service class 이용하면 필요 x
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        //
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);

        // 스프린트 미션 3 테스트
        clearDataFiles();
        clearDataFiles2();

        // BinaryContent 테스트를 위한 MultipartFile 변환
        MultipartFile testImageFile1;
        try {
            testImageFile1 = convertFileToMultipartFile("testFiles/userprofile.jpg");
        } catch (IOException e) {
            e.printStackTrace();
            testImageFile1 = null;
        }
        MultipartFile testImageFile2;
        try {
            testImageFile2 = convertFileToMultipartFile("testFiles/sky.jpg");
        } catch (IOException e) {
            e.printStackTrace();
            testImageFile2 = null;
        }

        // User Create
        UserCreateRequest ucr1 = new UserCreateRequest("Alice", "Alice@gmail.com", "password", null);
        User user1 = setupUser(userService, ucr1);
        UserCreateRequest ucr2 = new UserCreateRequest("Bob", "Bob@gmail.com", "password", testImageFile1);
        User user2 = setupUser(userService, ucr2);
        // 중복 검증
        setupUser(userService, new UserCreateRequest("Alice", "Alice@gmail.com", "password", null));

        // User Read
        readUser(userService, user1);
        readUser(userService, user2);
        readAllUsers(userService);

        //User Update
        updateUser(userService, user1, new UserUpdateRequest("Andy", "Andy@gmail.com", "password", testImageFile1));
        readUser(userService, user1);
        // User Delete
        deleteUser(userService, user2);
        System.out.println("After delete user2");
        readAllUsers(userService);

        // Channel Create - PUBLIC
        PublicChannelCreateRequest ccr1 = new PublicChannelCreateRequest("Ch 1", "This is public", null);
        Channel channel1 = setupPublicChannel(channelService, ccr1);
        // Channel Create - PRIVATE
        user2 = setupUser(userService, ucr2);
        readUser(userService, user2);
        PrivateChannelRequest ccr2 = new PrivateChannelRequest(List.of(user1.getId(), user2.getId()));
        Channel channel2 = setupPrivateChannel(channelService, ccr2);
        ccr2 = new PrivateChannelRequest(List.of(user2.getId()));
        Channel channel3 = setupPrivateChannel(channelService, ccr2);

        // Channel Read
        System.out.println("--- Read PUBLIC Channel ---");
        readChannel(channelService, user1, channel1);
        System.out.println("--- Read PRIVATE Channel ---");
        System.out.println("user1, user2가 참여한 channel 2를 user1이 조회");
        readChannel(channelService, user1, channel2);
        System.out.println("user1, user2가 참여한 channel 2를 user2이 조회");
        readChannel(channelService, user2, channel2);
        System.out.println("user2가 참여한 channel3를 user1이 조회");
        readChannel(channelService, user1, channel3);
        readChannelByUser(channelService, user1);

        // Channel Update
        ChannelUpdateRequest cur = new ChannelUpdateRequest("New Ch 1", "This is public!");
        updateChannel(channelService, channel1, cur);
        readChannel(channelService, user1, channel1);

        // Channel Delete
        deleteChannel(channelService, channel3);
        readChannel(channelService, user1, channel3);

        // Message Create
        MessageCreateRequest mcr1 = new MessageCreateRequest(user1.getId(), channel1.getId(), "Hi Ch 1", null);
        Message message1 = setupMessage(messageService, mcr1);
        // 입장하지 않은 private 채널에 메세지 생성 시도 시 예외 발생
        User user3 = setupUser(userService, new UserCreateRequest("Cindy", "Cindy@gmail.com", "12345", null));
        MessageCreateRequest mcr2 = new MessageCreateRequest(user3.getId(), channel2.getId(), "This message will not be sent", null);
        setupMessage(messageService, mcr2);
        // 메세지에 파일 첨부
        mcr1 = new MessageCreateRequest(user2.getId(), channel2.getId(), "Hi Ch 2", List.of(testImageFile1));
        Message message2 = setupMessage(messageService, mcr1);

        // Message Read
        readMessage(messageService, message1);
        readAllMessages(messageService);

        // Message Update
        updateMessage(messageService, message1, new MessageUpdateRequest("New Message"));
        readMessage(messageService, message1);

        // Message Delete
        deleteMessage(messageService, message2);
        readAllMessages(messageService);
        
        // UserStatus
        System.out.println(userStatusService.findByUserId(user1.getId()));
        System.out.println(userStatusService.findByUserId(user2.getId()));

        // ReadStatus
        System.out.println(readStatusService.findAllByUserId(user1.getId()));
        System.out.println(readStatusService.findAllByUserId(user2.getId()));

        //BinaryContent
        //User2 프로필이미지 확인
        System.out.println("User2's profile image: ");
        System.out.println(binaryContentService.findUserProfile(user2.getId()));
        //Message3 첨부파일 확인
        System.out.println("Attached Files in Message 3:");
        Message message3 = setupMessage(messageService, new MessageCreateRequest(user1.getId(),
                channel1.getId(), "Files", List.of(testImageFile1, testImageFile2)));
        List<BinaryContent> message3files = binaryContentService.findByMessageId(message3.getId());
        for (BinaryContent file : message3files) {
            System.out.println(file);
        }
    }

}
