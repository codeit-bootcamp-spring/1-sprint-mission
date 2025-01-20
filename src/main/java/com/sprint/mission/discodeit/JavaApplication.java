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
        System.out.println("== JCF 기반 테스트 ==");
        long startTime = System.nanoTime();
        testService(new JCFUserRepository(), new JCFChannelRepository(), new JCFMessageRepository());
        long endTime = System.nanoTime();
        System.out.printf("JCF 실행 시간: %.3f ms%n", (endTime - startTime) / 1_000_000.0);

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
        try {
            System.out.println("\n[사용자 등록]");
            User user1 = new User("홍길동", "hong@example.com");
            User user2 = new User("이순신", "lee@example.com");
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
            user1.updateUsername("홍길동(수정)");
            userRepository.update(user1.getId(), user1);
            System.out.printf("수정된 사용자: %s (이메일: %s)%n", user1.getUsername(), user1.getEmail());

            System.out.println("\n[사용자 삭제]");
            userRepository.delete(user1.getId());
            System.out.println(userRepository.findById(user1.getId()).isPresent() ?
                    "삭제 실패!" : "삭제 성공!");

            // 채널 등록 테스트
            System.out.println("\n[채널 등록]");
            Channel channel1 = new Channel("일반 채널", "잡담용 채널", user2);
            channelRepository.save(channel1);
            System.out.printf("채널 등록 완료: %s (설명: %s)%n", channel1.getName(), channel1.getDescription());

// 채널 수정 테스트
            System.out.println("\n[채널 수정]");
            channel1.updateDescription("업데이트된 잡담용 채널");
            channelRepository.update(channel1.getId(), channel1);
            System.out.printf("수정된 채널: %s (설명: %s)%n", channel1.getName(), channel1.getDescription());

// 채널 삭제 테스트
            System.out.println("\n[채널 삭제]");
            channelRepository.delete(channel1.getId());
            System.out.println(channelRepository.findById(channel1.getId()).isPresent() ?
                    "채널 삭제 실패!" : "채널 삭제 성공!");


            System.out.println("\n[메시지 등록]");
            Message message1 = new Message("안녕하세요!", user2.getId(), channel1.getId());
            messageRepository.save(message1);
            System.out.printf("메시지 등록 완료: %s (보낸이: %s)%n",
                    message1.getContent(), user2.getUsername());

            System.out.println("\n[메시지 수정]");
            message1.updateContent("안녕하세요! (수정)");
            messageRepository.update(message1.getId(), message1);
            System.out.printf("수정된 메시지: %s%n", messageRepository.findById(message1.getId()).get().getContent());

            System.out.println("\n[메시지 삭제]");
            messageRepository.delete(message1.getId());
            System.out.println(messageRepository.findById(message1.getId()).isPresent() ?
                    "메시지 삭제 실패!" : "메시지 삭제 성공!");

            System.out.println("\n[데이터 전체 조회]");
            System.out.println("사용자 목록:");
            userRepository.findAll().forEach(System.out::println);

            System.out.println("채널 목록:");
            channelRepository.findAll().forEach(System.out::println);

            System.out.println("메시지 목록:");
            messageRepository.findAll().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("테스트 실행 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
