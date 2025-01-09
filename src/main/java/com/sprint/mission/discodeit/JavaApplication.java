package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {
// 서비스 구현체 초기화
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // === User Service 테스트 ===
        System.out.println("=== User Service 테스트 ===");

        // 1. 등록
        User user1 = new User("user01", "password123", "크리스", "chris0123@gmail.com");
        User user2 = new User("user02", "password456", "밥", "bob060@gmail.com");
        userService.create(user1);
        userService.create(user2);

        // 2. 조회 (단건)
        System.out.println("\n[User 단건 조회]");
        Optional<User> retrievedUser = userService.read(user1);
        retrievedUser.ifPresent(user -> System.out.println("Found User: " + user.getUsername()));

        // 3. 조회 (다건)
        System.out.println("\n[User 다건 조회]");
        List<User> allUsers = userService.readAll();
        allUsers.forEach(user -> System.out.println("User: " + user.getUsername()));

        // 4. 수정
        System.out.println("\n[User 수정]");
        User updatedUser1 = new User("user01", "password123", "크리스지롱", "chris0123@gmail.com");
        updatedUser1.updateUserid(user1.getUserid());
        userService.update(user1, updatedUser1);

        // 5. 수정된 데이터 조회
        System.out.println("\n[수정된 User 조회]");
        Optional<User> updatedRetrievedUser = userService.read(user1);
        updatedRetrievedUser.ifPresent(user -> System.out.println("Updated User: " + user.getUsername()));

        // 6. 삭제
        System.out.println("\n[User 삭제]");
        boolean isDeleted = userService.delete(updatedUser1);
        System.out.println("Deleted: " + isDeleted);

        // 7. 삭제 확인
        System.out.println("\n[삭제된 User 확인]");
        Optional<User> deletedUserCheck = userService.read(updatedUser1);
        System.out.println(deletedUserCheck.isPresent() ? "삭제 실패" : "삭제 성공");


        // === Channel Service 테스트 ===
        System.out.println("\n=== Channel Service 테스트 ===");

        // 1. 등록
        Channel channel1 = new Channel("General", "General discussion", new HashSet<>(), new ArrayList<>());
        channel1.getParticipants().add(user2); // 사용자 추가
        channelService.create(channel1);

        // 2. 조회 (단건)
        System.out.println("\n[Channel 단건 조회]");
        Optional<Channel> retrievedChannel = channelService.read(channel1);
        retrievedChannel.ifPresent(channel -> System.out.println("Found Channel: " + channel.getName()));

        // 3. 조회 (다건)
        System.out.println("\n[Channel 다건 조회]");
        List<Channel> allChannels = channelService.readAll();
        allChannels.forEach(channel -> System.out.println("Channel: " + channel.getName()));

        // 4. 수정
        System.out.println("\n[Channel 수정]");
        Channel updatedChannel1 = new Channel("General Updated", "Updated discussion", new HashSet<>(), new ArrayList<>());
        updatedChannel1.getParticipants().add(user2); // 사용자 추가 유지
        channelService.update(channel1, updatedChannel1);

        // 5. 수정된 데이터 조회
        System.out.println("\n[수정된 Channel 조회]");
        Optional<Channel> updatedRetrievedChannel = channelService.read(channel1);
        updatedRetrievedChannel.ifPresent(channel -> System.out.println("Updated Channel: " + channel.getName()));

        // 6. 삭제
        System.out.println("\n[Channel 삭제]");
        boolean isChannelDeleted = channelService.delete(updatedChannel1);
        System.out.println("Deleted: " + isChannelDeleted);


        // === Message Service 테스트 ===
        System.out.println("\n=== Message Service 테스트 ===");

        // 1. 등록
        Message message1 = new Message("Hello, World!", user1, channel1);
        Message message2 = new Message("Hi there!", user2, channel1);

        messageService.create(message1);
        messageService.create(message2);

        // 2. 조회 (단건)
        System.out.println("\n[Message 단건 조회]");
        Optional<Message> retrievedMessage = messageService.read(user2);
        retrievedMessage.ifPresent(message -> System.out.println("Found Message: " + message.getContent()));

        // 3. 조회 (다건)
        System.out.println("\n[Message 다건 조회]");
        List<Message> allMessages = messageService.readAll();
        allMessages.forEach(message -> System.out.println("Message: " + message.getContent()));

        // 4. 수정
        System.out.println("\n[Message 수정]");
        Message updatedMessage1 = new Message("Updated Hello, World!", user2, channel1);
        messageService.updateByAuthor(user2, updatedMessage1);

        // 5. 수정된 데이터 조회
        System.out.println("\n[수정된 Message 조회]");
        Optional<Message> updatedRetrievedMessage = messageService.read(user2);
        updatedRetrievedMessage.ifPresent(message -> System.out.println("Updated Message: " + message.getContent()));

        // 6. 삭제
        System.out.println("\n[Message 삭제]");
        boolean isMessageDeleted = messageService.delete(message1.getId());
        System.out.println("Deleted: " + isMessageDeleted);

    }

}
