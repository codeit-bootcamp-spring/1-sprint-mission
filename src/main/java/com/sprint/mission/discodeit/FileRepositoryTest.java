package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.*;


public class FileRepositoryTest {
    public static void main(String[] args) {
        FileUserRepository fileUserRepository = new FileUserRepository();
        FileChannelRepository fileChannelRepository = new FileChannelRepository();
        FileMessageRepository fileMessageRepository = new FileMessageRepository();
        System.out.println("\n File repository test =======");

        //기존데이터 출력
        System.out.println("=== 기존 데이터 출력 ===");
        System.out.println("\n기존 유저 목록:");
        for (User user : fileUserRepository.findAll()) {
            System.out.println("유저명: " + user.getUsername());
        }

        System.out.println("\n기존 채널 목록:");
        for (Channel channel : fileChannelRepository.findAll()) {
            System.out.println("채널명: " + channel.getChannelName());
        }

        System.out.println("\n기존 메시지 목록:");
        for (Message message : fileMessageRepository.findAll()) {
            System.out.println("작성자: " + message.getUsername() + ", 내용: " + message.getContent());
        }

        System.out.println("\n=== 새 데이터 생성 ===");
        User user1 = new User("Alice", "password123");
        User user2 = new User("Belle", "password456");
        User user3 = new User("Charlie", "password789");

        fileUserRepository.save(user1);
        fileUserRepository.save(user2);
        fileUserRepository.save(user3);

        Channel channel1 = new Channel("channel1");
        Channel channel2 = new Channel("channel2");

        fileChannelRepository.save(channel1);
        fileChannelRepository.save(channel2);

        Message message1 = new Message(user1.getUsername(), channel1.getId(), "안녕하세요!");
        Message message2 = new Message(user2.getUsername(), channel1.getId(), "반갑습니다!");
        Message message3 = new Message(user3.getUsername(), channel2.getId(), "테스트입니다.");

        fileMessageRepository.save(message1);
        fileMessageRepository.save(message2);
        fileMessageRepository.save(message3);

        // 단건 조회
        System.out.println("\n=== 단건 조회 ===");
        User foundUser = fileUserRepository.findByUsername("Alice");
        System.out.println("\n유저 단건 조회: " + foundUser.getUsername());

        Channel foundChannel = fileChannelRepository.findByChannelname("channel1");
        System.out.println("\n채널 단건 조회: " + foundChannel.getChannelName());

        Message foundMessage = fileMessageRepository.findByMessageId(message1.getId());
        System.out.println("\n메시지 단건 조회: " + foundMessage.getContent());

        // 전체 조회
        System.out.println("\n=== 전체 조회 ===");
        System.out.println("\n유저 목록:");
        for (User user : fileUserRepository.findAll()) {
            System.out.println("유저명: " + user.getUsername());
        }

        System.out.println("\n채널 목록:");
        for (Channel channel : fileChannelRepository.findAll()) {
            System.out.println("채널명: " + channel.getChannelName());
        }

        System.out.println("\n메시지 목록:");
        for (Message message : fileMessageRepository.findAll()) {
            System.out.println("작성자: " + message.getUsername() + ", 내용: " + message.getContent());
        }

        // 삭제
        System.out.println("\n=== 삭제 실행 ===");
        fileUserRepository.deleteById(user3.getId());
        fileChannelRepository.deleteById(channel2.getId());
        fileMessageRepository.deleteById(message3.getId());

        // 삭제 후 전체 조회
        System.out.println("\n=== 삭제 후 전체 조회 ===");
        System.out.println("\n유저 목록:");
        for (User user : fileUserRepository.findAll()) {
            System.out.println("유저명: " + user.getUsername());
        }

        System.out.println("\n채널 목록:");
        for (Channel channel : fileChannelRepository.findAll()) {
            System.out.println("채널명: " + channel.getChannelName());
        }

        System.out.println("\n메시지 목록:");
        for (Message message : fileMessageRepository.findAll()) {
            System.out.println("작성자: " + message.getUsername() + ", 내용: " + message.getContent());
        }

    }
}