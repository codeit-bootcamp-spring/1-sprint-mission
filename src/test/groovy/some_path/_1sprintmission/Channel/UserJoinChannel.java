package some_path._1sprintmission.Channel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.service.jcf.JCFChannelService;
import some_path._1sprintmission.discodeit.service.jcf.JCFUserService;

@SpringBootApplication
public class UserJoinChannel {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();

        User user1 = new User("spring", "sff@nasvdf.com", "010-8425-4558");
        User user2 = new User("summer", "sff@nasvdf.com", "010-8425-4558");
        User user3 = new User("fall", "sff@nasvdf.com", "010-8425-4558");
        User over_user = new User("winter", "sff@nasvdf.com", "010-8425-4558");

        Channel channel = new Channel("example Channel");

        user1.joinChannel(channel);
        user2.joinChannel(channel);
        user3.joinChannel(channel);
        channel.getMembers().stream()
                .forEach(u -> System.out.println(u.getUsername()));

        //limit Person error
        //over_user.joinChannel(channel);
        user1.leaveChannel(channel);
        channel.getMembers().stream()
                .forEach(u -> System.out.println(u.getUsername()));

    }
}
