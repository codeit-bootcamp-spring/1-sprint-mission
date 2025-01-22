import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.factory.FileRepositoryFactory;
import com.sprint.mission.discodeit.factory.FileServiceFactory;
import com.sprint.mission.discodeit.factory.JCFRepositoryFactory;
import com.sprint.mission.discodeit.factory.RepositoryFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.util.ChannelType;
import com.sprint.mission.discodeit.util.FileIOUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RepositoryTest {
    RepositoryFactory factory = FileRepositoryFactory.getInstance();
    UserService userService = new BasicUserService(factory.getUserRepository());
    ChannelService channelService = new BasicChannelService(factory.getChannelRepository());
    MessageService messageService = new BasicMessageService(factory.getMessageRepository(), factory.getUserRepository(), factory.getChannelRepository());

    @BeforeEach
    void before() {
        try {
            Files.deleteIfExists(Paths.get("./result/users.ser"));
            Files.deleteIfExists(Paths.get("./result/messages.ser"));
            Files.deleteIfExists(Paths.get("./result/channels.ser"));
        } catch (IOException e) {
            System.err.println("파일 초기화 중 오류 발생: " + e.getMessage());
        }
    }

    @AfterEach
    void after() {
        if (factory instanceof FileRepositoryFactory) {
            FileIOUtil.convertDSerToJson(Paths.get("./result/users.ser"), Paths.get("./json/users.json"));
            FileIOUtil.convertDSerToJson(Paths.get("./result/messages.ser"), Paths.get("./json/messages.json"));
            FileIOUtil.convertDSerToJson(Paths.get("./result/channels.ser"), Paths.get("./json/channels.json"));
        }
    }

    @Test
    @DisplayName("메시지 생성 확인")
    void createMessage() {
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
      }


    User setupUser(UserService userService) {
        User user = userService.create("woody", "010-9218-1188", "Abcdefgh12312!");
        return user;
    }

    Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create("코드잇 공지 채널", "공지 채널입니다", ChannelType.VOICE);
        return channel;
    }

    void messageCreateTest(MessageService messageService, Channel channel, User writer) {
        Message message = messageService.create("안녕하세요", writer, channel);
        System.out.println("메시지 생성 : " + message);
    }
}
