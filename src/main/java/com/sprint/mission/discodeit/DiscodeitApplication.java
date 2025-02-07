package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.basic.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.repository.file.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        // Spring Context 시작
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        //  Spring Bean에서 Service와 Repository 가져오기
        UserService userService = context.getBean(BasicUserService.class);
        ChannelService channelService = context.getBean(BasicChannelService.class);
        MessageService messageService = context.getBean(BasicMessageService.class);

        // 3️JCF 기반 Repository 생성
        UserRepository userRepo = new JCFUserRepository();
        ChannelRepository channelRepo = new JCFChannelRepository();
        MessageRepository messageRepo = new JCFMessageRepository();

        // File 기반 Repository 생성
        UserRepository fileUserRepo = new FileUserRepository();
        ChannelRepository fileChannelRepo = new FileChannelRepository();
        MessageRepository fileMessageRepo = new FileMessageRepository();

        //  테스트 시작
        System.out.println(" Spring Boot 실행 및 Repository 테스트");

        //  1. User 생성 & 저장 테스트 (JCF)
        UUID userId = testUser(userRepo);
        UUID fileUserId = testUser(fileUserRepo); // File 기반도 테스트

        //  2. Channel 생성 & 저장 테스트 (JCF)
        UUID channelId = testChannel(channelRepo);
        UUID fileChannelId = testChannel(fileChannelRepo); // File 기반도 테스트

        //  3. Message 생성 & 저장 테스트 (JCF)
        testMessage(messageRepo, userId, channelId);
        testMessage(fileMessageRepo, fileUserId, fileChannelId); // File 기반도 테스트

        System.out.println(" 모든 Repository & Service 정상 동작 확인");
    }

    //  User 생성 테스트
    private static UUID testUser(UserRepository userRepo) {
        User user = new User("woody", "woody@codeit.com", "woody1234");
        userRepo.save(user);
        Optional<User> foundUser = userRepo.findById(user.getId());

        if (foundUser.isPresent()) {
            System.out.println("User 저장 성공: " + foundUser.get().getUsername());
        } else {
            System.out.println("User 저장 실패");
        }

        return user.getId();
    }

    //  Channel 생성 테스트
    private static UUID testChannel(ChannelRepository channelRepo) {
        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지 채널입니다.", null);
        channelRepo.save(channel);
        Optional<Channel> foundChannel = channelRepo.findById(channel.getId());

        if (foundChannel.isPresent()) {
            System.out.println(" Channel 저장 성공: " + foundChannel.get().getName());
        } else {
            System.out.println(" Channel 저장 실패");
        }

        return channel.getId();
    }

    //  Message 생성 테스트
    private static void testMessage(MessageRepository messageRepo, UUID userId, UUID channelId) {
        Message message = new Message("안녕하세요.", channelId, userId);
        messageRepo.save(message);
        Optional<Message> foundMessage = messageRepo.findById(message.getId());

        if (foundMessage.isPresent()) {
            System.out.println("Message 저장 성공: " + foundMessage.get().getContent());
        } else {
            System.out.println("Message 저장 실패");
        }
    }
}
