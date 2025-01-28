package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;

import java.time.Instant;

public class JavaApplication {
    public static void main(String[] args) {
        // JCF 기반 테스트
        System.out.println("== JCF 기반 테스트 ==");
        long startTime = System.nanoTime();
        testService(new JCFUserRepository(), new JCFChannelRepository(), new JCFMessageRepository());
        long endTime = System.nanoTime();
        System.out.printf("JCF 실행 시간: %.3f ms%n", (endTime - startTime) / 1_000_000.0);

        // File IO 기반 테스트
        System.out.println("\n== File IO 기반 테스트 ==");
        startTime = System.nanoTime();
        testService(new FileUserRepository(), new FileChannelRepository(), new FileMessageRepository());
        endTime = System.nanoTime();
        System.out.printf("File IO 실행 시간: %.3f ms%n", (endTime - startTime) / 1_000_000.0);
    }

    private static void testService(
            UserRepository userRepository,
            ChannelRepository channelRepository,
            MessageRepository messageRepository) {
        System.out.println("\n[사용자 등록]");
        User user1 = new User("Amy", "amy@example.com");
        User user2 = new User("Chris", "chris@example.com");
        userRepository.save(user1);
        userRepository.save(user2);
        System.out.printf("사용자 등록 완료: %s (%s)%n", user1.getUsername(), user1.getEmail());
        System.out.printf("사용자 등록 완료: %s (%s)%n", user2.getUsername(), user2.getEmail());

        System.out.println("\n[사용자 전체 조회]");
        userRepository.findAll().forEach(user ->
                System.out.printf("  - %s (이메일: %s, 생성일: %s)%n",
                        user.getUsername(), user.getEmail(), Instant.ofEpochMilli(user.getCreatedAt()))
        );

        System.out.println("\n[사용자 수정]");
        user1.updateUsername("Amy Updated");
        userRepository.update(user1.getId(), user1);
        System.out.printf("수정된 사용자: %s (이메일: %s)%n", user1.getUsername(), user1.getEmail());

        System.out.println("\n[사용자 삭제]");
        userRepository.delete(user1.getId());
        System.out.println(userRepository.findById(user1.getId()).isPresent() ?
                "삭제 실패!" : "삭제 성공!");

        System.out.println("\n[채널 등록]");
        Channel channel1 = new Channel("Second 채널", "업데이트된 채널 설명", user2);
        channelRepository.save(channel1);
        System.out.printf("채널 등록 완료: %s (설명: %s)%n", channel1.getName(), channel1.getDescription());

        System.out.println("\n[메시지 등록]");
        Message message1 = new Message("안녕하세요!", user2.getId(), channel1.getId());
        messageRepository.save(message1);
        System.out.printf("메시지 등록 완료: %s (보낸이: %s)%n", message1.getContent(), user2.getUsername());

        System.out.println("\n[메시지 삭제]");
        messageRepository.delete(message1.getId());
        System.out.println(messageRepository.findById(message1.getId()).isPresent() ?
                "메시지 삭제 실패!" : "메시지 삭제 성공!");
    }
}
