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

        System.out.println("사용자 등록");
        User user1 = new User("daemin", "daemin@gmail.com");
        User user2 = new User("ayden", "ayden@gmail.com");
        userService.createUser(user1);
        userService.createUser(user2);

        System.out.println("");

        System.out.println("사용자 조회 단건");
        System.out.println(userService.readUser(user1.getId().toString()));
        System.out.println(userService.readUser(user2.getId().toString()));

        System.out.println("");

        System.out.println("사용자 조회 다건 ");
        List<User> allUsers = userService.readAllUsers();
        for (User user : allUsers) {
            System.out.println(user);
        }
    }
}
