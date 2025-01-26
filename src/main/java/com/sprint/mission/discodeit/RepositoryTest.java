    package com.sprint.mission.discodeit;
    import com.sprint.mission.discodeit.repository.*;
    import com.sprint.mission.discodeit.repository.file.*;
    import com.sprint.mission.discodeit.repository.jcf.*;
    import com.sprint.mission.entity.*;

    import java.util.List;
    import java.util.UUID;

    public class RepositoryTest {
        public static void main(String[] args) {
            // JCF 테스트
            System.out.println("=== JCF Repository 테스트 ===");
            testRepositories(new JCFUserRepository(), new JCFChannelRepository(), new JCFMessageRepository());
        }

        private static void testRepositories(UserRepository userRepository, ChannelRepository channelRepository, MessageRepository messageRepository) {


            // Jack, Bob 계정 생성
            User jack = new User(UUID.randomUUID(), "codeit@codeit.com", "Jack");
            User bob = new User(UUID.randomUUID(), "yeoksam2@codeit.com", "Bob");
            userRepository.saveUser(jack);
            userRepository.saveUser(bob);


            // 전체 유저 조회
            System.out.println("모든 유저 조회 :\n" + userRepository.printAllUser());


            // Bob 계정 삭제
            System.out.println("Bob 계정 삭제");
            userRepository.deleteUser(bob);
            System.out.println("모든 유저 조회 :\n" + userRepository.printAllUser());


            // Jack, Bob의 채널 생성 -> 각 유저마다 채널 객체 생성
            Channel channel1 = new Channel(jack, "codeit");
            Channel channel2 = new Channel(bob, "codeit2");
            channelRepository.saveChannel(channel1);
            channelRepository.saveChannel(channel2);

            // 전체 채널 조회
            System.out.println("모든 채널 조회 :\n" + channelRepository.printAllChannel());

            // 채널 삭제
            channelRepository.deleteChannel(channel2);
            System.out.println("모든 채널 조회 :\n" + channelRepository.printAllChannel());

            // 메시지 생성 -> 채널에 메시지 객체 생성
            Message message1 = new Message(jack, channel1, "메시지 생성1");
            Message message2 = new Message(bob, channel2, "메시지 생성2");
            messageRepository.saveMessage(message1);
            messageRepository.saveMessage(message2);
            messageRepository.printByUser(jack);
            System.out.println("모든 메시지 조회 :\n" + messageRepository.printAllMessage());

            // 특정 유저 메시지 조회
            messageRepository.printByUser(bob);
            System.out.println(messageRepository.printByUser(bob));

            // 메시지 삭제
            messageRepository.deleteMessage(message2);
            messageRepository.printAllMessage();
            System.out.println("모든 메시지 조회");
            System.out.println(messageRepository.printAllMessage());
        }
    }
