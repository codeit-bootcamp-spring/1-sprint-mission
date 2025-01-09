import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {
        // 1. 서비스 구현체 생성
        JCFUserService userService = new JCFUserService();
        JCFMessageService messageService = new JCFMessageService();
        JCFChannelService channelService = new JCFChannelService();

        // === [User 테스트] ===
        System.out.println("=== User 테스트 ===");

        // 2. 등록
        User user1 = new User("user01", "password123", "John Doe", "john@example.com");
        User user2 = new User("user02", "password456", "Jane Smith", "jane@example.com");
        userService.save(user1);
        userService.save(user2);

        // 3. 조회 (단건)
        System.out.println("읽기:");
        Optional<User> retrievedUser = userService.read(user1.getId());
        retrievedUser.ifPresent(System.out::println);

        // 4. 조회 (다건)
        System.out.println("전부 읽기:");
        List<User> allUsers = userService.readAll();
        allUsers.forEach(System.out::println);

        // 5. 수정
        System.out.println("수정:");
        user1.updateUsername("John Updated");
        user1.updateUserEmail("updated_john@example.com");
        userService.update(user1.getId(), user1);

        // 6. 수정된 데이터 조회
        System.out.println("수정된 데이터 조회:");
        Optional<User> updatedUser = userService.read(user1.getId());
        updatedUser.ifPresent(System.out::println);

        // 7. 삭제
        System.out.println("삭제:");
        boolean isDeleted = userService.delete(user1.getId());
        System.out.println("삭제 여부: " + isDeleted);

        // 8. 삭제 확인 (조회)
        System.out.println("삭제 확인:");
        Optional<User> deletedUser = userService.read(user1.getId());
        System.out.println(deletedUser.isPresent() ? "삭제 실패" : "삭제 성공");


        // === [Channel 테스트] ===
        System.out.println("\n=== Channel 테스트 ===");

        // 2. 등록
        Channel channel1 = new Channel("General", "General discussion", new HashSet<>(), new ArrayList<>());
        Channel channel2 = new Channel("Tech", "Technology discussion", new HashSet<>(), new ArrayList<>());
        channelService.save(channel1);
        channelService.save(channel2);

        // 3. 조회 (단건)
        System.out.println("읽기:");
        Optional<Channel> retrievedChannel = channelService.read(channel1.getId());
        retrievedChannel.ifPresent(System.out::println);

        // 4. 조회 (다건)
        System.out.println("전부 읽기:");
        List<Channel> allChannels = channelService.readAll();
        allChannels.forEach(System.out::println);

        // 5. 수정
        System.out.println("수정:");
        channel1.setName("Updated General");
        channel1.setDescription("Updated General discussion");
        channelService.update(channel1.getId(), channel1);

        // 6. 수정된 데이터 조회
        System.out.println("수정된 데이터 조회:");
        Optional<Channel> updatedChannel = channelService.read(channel1.getId());
        updatedChannel.ifPresent(System.out::println);

        // 7. 삭제
        System.out.println("삭제:");
        boolean isChannelDeleted = channelService.delete(channel1.getId());
        System.out.println("삭제 여부: " + isChannelDeleted);


        // === [Message 테스트] ===
        System.out.println("\n=== Message 테스트 ===");

        // 2. 등록
        Message message1 = new Message("Hello, World!", user2.getId().toString(), channel2.getId().toString());
        Message message2 = new Message("How's it going?", user2.getId().toString(), channel2.getId().toString());
        messageService.save(message1);
        messageService.save(message2);

        // 3. 조회 (단건)
        System.out.println("읽기:");
        Optional<Message> retrievedMessage = messageService.read(message1.getId());
        retrievedMessage.ifPresent(System.out::println);

        // 4. 조회 (다건)
        System.out.println("전부 읽기:");
        List<Message> allMessages = messageService.readAll();
        allMessages.forEach(System.out::println);

        // 5. 수정
        System.out.println("수정:");
        message1.updateContent("Updated Hello, World!");
        messageService.update(message1.getId(), message1);

        // 6. 수정된 데이터 조회
        System.out.println("수정된 데이터 조회:");
        Optional<Message> updatedMessage = messageService.read(message1.getId());
        updatedMessage.ifPresent(System.out::println);

        // 7. 삭제
        System.out.println("삭제:");
        boolean isMessageDeleted = messageService.delete(message1.getId());
        System.out.println("삭제 여부: " + isMessageDeleted);

    }
}
