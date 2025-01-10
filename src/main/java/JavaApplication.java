import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        //도메인 별 서비스 생성
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        System.out.println("\n사용자 등록");
        User user1 = new User("daemin", "daemin@gmail.com");
        User user2 = new User("ayden", "ayden@gmail.com");
        userService.createUser(user1);
        userService.createUser(user2);



        System.out.println("\n사용자 조회 단건");
        System.out.println(userService.readUser(user1.getId().toString()));
        System.out.println(userService.readUser(user2.getId().toString()));



        System.out.println("\n사용자 조회 다건 ");
        List<User> allUsers = userService.readAllUsers();
        for (User user : allUsers) {
            System.out.println(user);
        }



        System.out.println("\n사용자 수정");
        user1.updateEmail("updatedaemin@gmail.com");
        userService.updateUser(user1);


        System.out.println("\n수정된 사용자 조회");
        System.out.println(userService.readUser(user1.getId().toString()));



        System.out.println("\n사용자 삭제");
        userService.deleteUser(user2.getId().toString());



        System.out.println("\n삭제된 사용자 조회");
        try {
            userService.readUser(user2.getId().toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n채널 등록");
        Channel channel1 = new Channel("study Q&A", "study qustion and answer", false, new ArrayList<>());
        channelService.createChannel(channel1);



        System.out.println("\n채널 조회");
        System.out.println(channelService.readChannel(channel1.getId().toString()));


        System.out.println("\n채널 수정");
        channel1.updateDescription("Updated description for study Q&A");
        channelService.updateChannel(channel1);



        System.out.println("\n수정된 채널 조회");
        System.out.println(channelService.readChannel(channel1.getId().toString()));



        System.out.println("\n멤버 추가");
        channelService.addMember(channel1.getId().toString(), user1);
        channelService.addMember(channel1.getId().toString(), user2);

        System.out.println("\n멤버가 추가된 채널: " + channelService.readChannel(channel1.getId().toString()));



        System.out.println("\n멤버 삭제");
        channelService.removeMember(channel1.getId().toString(), user2);
        System.out.println("\n멤버가 삭제 된 채널: " + channelService.readChannel(channel1.getId().toString()));



        System.out.println("\n채널 삭제");
        channelService.deleteChannel(channel1.getId().toString());
        System.out.println("\n삭제된 채널 ID: " + channel1.getId());



        System.out.println("\n삭제 확인");
        try {
            System.out.println(channelService.readChannel(channel1.getId().toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        System.out.println("\n메시지 등록");
        Message message1 = new Message("안녕 친구들", "daemin", "channel1");
        Message message2 = new Message("공부는 잘 돼 다들?", "Bob", "channel1");

        messageService.createMessage(message1);
        messageService.createMessage(message2);

        System.out.println("\n메시지 조회");
        System.out.println(messageService.readMessage(message1.getId().toString()));
        System.out.println(messageService.readMessage(message2.getId().toString()));
        System.out.println("");

        System.out.println("\n메시지 수정");
        message1.updateContent("Hello friends! Updated content.");
        messageService.updateMessage(message1);
        System.out.println("\n수정된 메시지 조회 ===");
        System.out.println(messageService.readMessage(message1.getId().toString()));
        System.out.println("\n메시지 삭제");
        messageService.deleteMessage(message2.getId().toString());
        System.out.println("\n삭제 확인");
        try {
            System.out.println(messageService.readMessage(message2.getId().toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
}
