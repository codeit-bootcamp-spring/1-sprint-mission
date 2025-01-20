import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.ArrayList;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
//        FileUserService userService = new FileUserService();

        // 파일 초기화
//        userService.resetFile();

        // 사용자 추가
//        User user1 = new User("Alice", "alice@example.com");
//        User user2 = new User("Bob", "bob@example.com");
//        User user3 = new User("Charlie", "charlie@example.com");
//        userService.createUser(user1);
//        userService.createUser(user2);
//        userService.createUser(user3);
//
//        // 저장 후 데이터 확인
//        System.out.println("파일 저장 후 사용자 목록:");
//        userService.readAllUsers().forEach(System.out::println);
//
//        // 특정 사용자 검색
//        try {
//            System.out.println("ID 1 사용자: " + userService.readUser(user2.getId().toString()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//  메시지 테스트
//        // 특정 사용자 업데이트
//        System.out.println("\n 업데이트 : alice의 이름 변경");
//        user1.updateEmail("alice@newemail.com");
//        userService.updateUser(user1);
//        userService.readAllUsers().forEach(System.out::println);
//
//        // 특정 사용자 삭제
//        System.out.println("특정 사용자 삭제");
//        userService.deleteUser(user1.getId().toString());
//        userService.readAllUsers().forEach(System.out::println);
//
//        FileMessageService messageService = new FileMessageService();
//
//        messageService.resetFile();
//
//        System.out.println("\n메시지 생성 테스트");
//        Message message1 = new Message("hello", "daemin", "general");
//        Message message2 = new Message("hello daemin", "juhyeon", "general");
//
//        messageService.createMessage(message1);
//        messageService.createMessage(message2);
//
//        System.out.println("\n파일 저장 후 메시지 목록:");
//        messageService.readAllMessages().forEach(System.out::println);
//
//        try {
//            System.out.println(messageService.readMessage(message2.getId().toString()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("\n 업데이트 : daemin의 이름 변경");
//        message1.updateAuthor("buhyeon");
//        messageService.updateMessage(message1);
//        messageService.readAllMessages().forEach(System.out::println);
//
//        System.out.println("특정 메세지 삭제");
//        messageService.deleteMessage(message1.getId().toString());
//        messageService.readAllMessages().forEach(System.out::println);

        FileChannelService channelService = new FileChannelService();

        // 파일 초기화
        channelService.resetFile();

        System.out.println("\n채널 생성 테스트");
        Channel channel1 = new Channel("General", "Main discussion channel", false, new ArrayList<>());
        Channel channel2 = new Channel("Random", "Casual chat", false, new ArrayList<>());

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);

        System.out.println("\n파일 저장 후 채널 목록:");
        channelService.readAllChannel().forEach(System.out::println);

        System.out.println("\n멤버 추가 및 제거 테스트");
        // 멤버 생성
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");

        // 멤버 추가
        System.out.println("\n멤버 추가: Alice, Bob -> General Chat");
        channelService.addMember(channel1.getId().toString(), user1);
        channelService.addMember(channel1.getId().toString(), user2);


        try {
            System.out.println("\n특정 채널 읽기:");
            System.out.println(channelService.readChannel(channel1.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n업데이트: 채널 이름 변경 (General -> General Chat)");
        channel1.updateName("General Chat");
        channelService.updateChannel(channel1);

        // 채널 상태 확인
        channelService.readAllChannel().forEach(System.out::println);

        // 멤버 제거
        System.out.println("\n멤버 제거: Alice -> General Chat");
        channelService.removeMember(channel1.getId().toString(), user1);

        // 채널 상태 확인
        channelService.readAllChannel().forEach(System.out::println);

        System.out.println("\n특정 채널 삭제");
        channelService.deleteChannel(channel1.getId().toString());
        channelService.readAllChannel().forEach(System.out::println);

    }


}