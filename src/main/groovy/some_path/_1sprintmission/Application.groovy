package some_path._1sprintmission


import org.springframework.boot.autoconfigure.SpringBootApplication
import some_path._1sprintmission.discodeit.entiry.Channel
import some_path._1sprintmission.discodeit.entiry.Message
import some_path._1sprintmission.discodeit.entiry.User
import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus
import some_path._1sprintmission.discodeit.entiry.validation.Email
import some_path._1sprintmission.discodeit.entiry.validation.Phone
import some_path._1sprintmission.discodeit.service.jcf.JCFChannelService
import some_path._1sprintmission.discodeit.service.jcf.JCFMessageService
import some_path._1sprintmission.discodeit.service.jcf.JCFUserService

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // 서비스 객체 생성
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // **User 테스트**
        System.out.println("\n=== User 테스트 ===");
        User user = new User("User1","asdf@asdf.com", "010-8654-4532");
        User user2 = new User("User2","qwer@asdf.com", "010-8654-1111");

        userService.createUser(user); // 등록
        userService.createUser(user2); // 등록

        System.out.println("\n=== 단건 조회 ===");
        userService.getUser(user.getId()); //단건 조회
        System.out.println("\n=== 다건 조회 ===");
        userService.getUserAll().stream().forEach {s -> userService.getUser(s.getId())}; //다건 조회

        System.out.println("\n=== 유저 이름 수정 ===");
        user.setUsername("haram"); // 수정
        user.setDiscodeStatus(DiscodeStatus.DEACTIVATE);
        user.setIntroduce("안녕하세요")
        System.out.println("수정된 데이터 조회: " + userService.getUser(user.getId())); // 수정된 데이터 조회

        userService.deleteUser(user2.getId()); // 삭제
        userService.getUserAll().stream().forEach {s -> System.out.println("유저 이름 : " + s.getUsername())};

        // **Channel 테스트**
        System.out.println("\n=== Channel 테스트 ===");
        Channel channel = new Channel("Channel1");
        Channel channel2 = new Channel("Channel2");
        channelService.createChannel(channel); // 등록
        channelService.createChannel(channel2); // 등록
        System.out.println("단건 조회: " + channelService.getChannel(channel.getId())); // 단건 조회
        System.out.println("다건 조회: " + channelService.getChannelAll()); // 다건 조회

        channel.updateName("Announcements"); // 수정
        channelService.updateChannel(channel.getId(), channel); // 수정 반영
        System.out.println("수정된 데이터 조회: " + channelService.getChannel(channel.getId())); // 수정된 데이터 조회

        channelService.deleteChannel(channel.getId()); // 삭제
        System.out.println("삭제 후 조회: " + channelService.getChannel(channel.getId())); // 삭제 확인

        user.joinChannel(channel);
        user.joinChannel(channel2);

        user2.joinChannel(channel);
        user2.joinChannel(channel2);
        System.out.println("\n=== Channels ===");
        System.out.println(channel);
        System.out.println(channel2);

        user.leaveChannel(channel);
        System.out.println("\n=== After User1 leaves General ===");
        System.out.println(channel.getMembers());
        System.out.println(user.getChannels());

        // **Message 테스트**
        System.out.println("\n=== Message 테스트 ===");

        User newUser = new User("User1","asdf@asdf.com", "010-8654-4532");
        User newUser2 = new User("User2","asdf@asdf.com", "010-8654-4532");
        userService.createUser(newUser);
        userService.createUser(newUser2);

        Channel newChannel = new Channel("Support");
        channelService.createChannel(newChannel);

        newChannel.addMember(newUser);
        newChannel.addMember(newUser2);
        Message message = new Message(newUser, "Hi hello");
        Message message2 = new Message(newUser2, "Hi how are you?");
        messageService.create(message, newChannel, message.getSender());
        messageService.create(message2, newChannel, message2.getSender());

        System.out.println("Channel Messages: " + newChannel.getMessages());

    }
}
