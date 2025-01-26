package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    // 유틸리티 메서드: 사용자 생성
    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com");
        System.out.println("사용자 생성: " + user);
        return user;
    }

    // 유틸리티 메서드: 채널 생성
    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create("공지 채널");
        System.out.println("채널 생성: " + channel);
        return channel;
    }

    // 유틸리티 메서드: 메시지 생성 테스트
    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", author.getId(), channel.getId());
        System.out.println("메시지 생성: " + message);
    }

    // 유틸리티 메서드: 모든 사용자 출력
    static void printAllUsers(UserService userService) {
        List<User> users = userService.findAll();
        System.out.println("=== 모든 사용자 ===");
        for (User user : users) {
            System.out.println(user);
        }
    }

    // 유틸리티 메서드: 모든 채널 출력
    static void printAllChannels(ChannelService channelService) {
        List<Channel> channels = channelService.findAll();
        System.out.println("=== 모든 채널 ===");
        for (Channel channel : channels) {
            System.out.println(channel);
        }
    }

    // 유틸리티 메서드: 모든 메시지 출력
    static void printAllMessages(MessageService messageService) {
        List<Message> messages = messageService.findAll();
        System.out.println("=== 모든 메시지 ===");
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        // ============================
        // [A] Basic Service + JCF Repository
        // ============================
        System.out.println("=== [Basic*Service + JCF*Repository] ===");
        UserRepository jcfUserRepo = new JCFUserRepository();
        ChannelRepository jcfChannelRepo = new JCFChannelRepository();
        MessageRepository jcfMessageRepo = new JCFMessageRepository();

        UserService basicJcfUserService = new BasicUserService(jcfUserRepo);
        ChannelService basicJcfChannelService = new BasicChannelService(jcfChannelRepo);
        MessageService basicJcfMessageService = new BasicMessageService(jcfMessageRepo, basicJcfUserService, basicJcfChannelService);

        // 셋업
        User jcfUser = setupUser(basicJcfUserService);
        Channel jcfChannel = setupChannel(basicJcfChannelService);
        messageCreateTest(basicJcfMessageService, jcfChannel, jcfUser);

        // 조회
        System.out.println("\n--- 조회 ---");
        User fetchedJcfUser = basicJcfUserService.findById(jcfUser.getId());
        System.out.println("조회된 사용자: " + fetchedJcfUser);

        Channel fetchedJcfChannel = basicJcfChannelService.findById(jcfChannel.getId());
        System.out.println("조회된 채널: " + fetchedJcfChannel);

        // 모든 사용자, 채널, 메시지 출력
        printAllUsers(basicJcfUserService);
        printAllChannels(basicJcfChannelService);
        printAllMessages(basicJcfMessageService);

        // 수정
        System.out.println("\n--- 수정 ---");
        basicJcfUserService.update(jcfUser.getId(), "woody_updated", "woody_updated@codeit.com");
        basicJcfChannelService.update(jcfChannel.getId(), "공지 채널 (수정)");

        // 수정된 데이터 조회
        User updatedJcfUser = basicJcfUserService.findById(jcfUser.getId());
        System.out.println("수정된 사용자: " + updatedJcfUser);

        Channel updatedJcfChannel = basicJcfChannelService.findById(jcfChannel.getId());
        System.out.println("수정된 채널: " + updatedJcfChannel);

        // 삭제
        System.out.println("\n--- 삭제 ---");
        basicJcfMessageService.delete(basicJcfMessageService.findAll().get(0).getId());
        basicJcfUserService.delete(jcfUser.getId());
        basicJcfChannelService.delete(jcfChannel.getId());

        // 삭제 후 데이터 조회
        printAllUsers(basicJcfUserService);
        printAllChannels(basicJcfChannelService);
        printAllMessages(basicJcfMessageService);

        // ============================
        // [B] Basic Service + File Repository
        // ============================
        System.out.println("\n=== [Basic*Service + File*Repository] ===");
        UserRepository fileUserRepo = new FileUserRepository();
        ChannelRepository fileChannelRepo = new FileChannelRepository();
        MessageRepository fileMessageRepo = new FileMessageRepository();

        UserService basicFileUserService = new BasicUserService(fileUserRepo);
        ChannelService basicFileChannelService = new BasicChannelService(fileChannelRepo);
        MessageService basicFileMessageService = new BasicMessageService(fileMessageRepo, basicFileUserService, basicFileChannelService);

        // 셋업
        User fileUser = setupUser(basicFileUserService);
        Channel fileChannel = setupChannel(basicFileChannelService);
        messageCreateTest(basicFileMessageService, fileChannel, fileUser);

        // 조회
        System.out.println("\n--- 조회 ---");
        User fetchedFileUser = basicFileUserService.findById(fileUser.getId());
        System.out.println("조회된 사용자: " + fetchedFileUser);

        Channel fetchedFileChannel = basicFileChannelService.findById(fileChannel.getId());
        System.out.println("조회된 채널: " + fetchedFileChannel);

        // 모든 사용자, 채널, 메시지 출력
        printAllUsers(basicFileUserService);
        printAllChannels(basicFileChannelService);
        printAllMessages(basicFileMessageService);

        // 수정
        System.out.println("\n--- 수정 ---");
        basicFileUserService.update(fileUser.getId(), "file_woody_updated", "file_woody_updated@codeit.com");
        basicFileChannelService.update(fileChannel.getId(), "공지 채널 (파일 수정)");

        // 수정된 데이터 조회
        User updatedFileUser = basicFileUserService.findById(fileUser.getId());
        System.out.println("수정된 사용자: " + updatedFileUser);

        Channel updatedFileChannel = basicFileChannelService.findById(fileChannel.getId());
        System.out.println("수정된 채널: " + updatedFileChannel);

        // 삭제
        System.out.println("\n--- 삭제 ---");
        basicFileMessageService.delete(basicFileMessageService.findAll().get(0).getId());
        basicFileUserService.delete(fileUser.getId());
        basicFileChannelService.delete(fileChannel.getId());

        // 삭제 후 데이터 조회
        printAllUsers(basicFileUserService);
        printAllChannels(basicFileChannelService);
        printAllMessages(basicFileMessageService);
    }
}

