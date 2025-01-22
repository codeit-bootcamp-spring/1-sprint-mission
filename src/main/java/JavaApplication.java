import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();

        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(user, channelService);

        // 테스트
        messageCreateTest(messageService, channel, user);
    }

    static User setupUser(UserService userService) {
        //User user = userService.createUser("woody", "woody@codeit.com", "woody1234");
        UUID userId = userService.createUser("woody");
        return userService.getUserById(userId);
    }

    static Channel setupChannel(User owner, ChannelService channelService) {
        //Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        UUID channelId = channelService.createChannel(owner, "공지 채널");
        return channelService.getChannelById(channelId);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        //Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        UUID messageId = messageService.createMessage(channel, "안녕하세요.");
        System.out.println("메시지 생성: " + messageService.getMessageById(messageId));
    }
}
