import com.sprint.mission.discodeit.config.ApplicationBuilder;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.ChannelFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.sprint.mission.discodeit.constant.FileConstant.*;

public class ApplicationTest {

  static ChannelService channelService;
  static UserService userService;
  static MessageServiceV2<ChatChannel> messageServiceV2;
  static ChannelFactory channelFactory;
  static ApplicationBuilder builder;

  @BeforeAll
  static void setupClass() {
    builder = new ApplicationBuilder();

    channelService = builder.getChannelService();
    userService = builder.getUserService();
    messageServiceV2 = builder.getMessageService();

    channelFactory = new ChannelFactory();
  }

  @BeforeEach
  void beforeEach() {
    File file = new File(MESSAGE_FILE);
    File file2 = new File(USER_FILE);
    File file3 = new File(CHANNEL_FILE);

    if (file.exists()) {
      file.delete();
    }
    if (file2.exists()) {
      file2.delete();
    }
    if (file3.exists()) {
      file3.delete();
    }
  }

  @Test
  void ifTwoInstanceShareSame() {
    User user = userService.createUser(
        new User.UserBuilder("user1", "pwd1", "example@gmail.com")
            .phoneNumber("01012345678")
            .nickname("user1Nick")
            .build()
    );

    ChatChannel ch = channelFactory.createChatChannel("11111","11111","chatChannel", 50);

    channelService.createChannel(ch);

    messageServiceV2.createMessage(user.getUUID(), new Message.MessageBuilder(user.getUUID(), ch.getUUID(), "content").build(), ch  );

    Assertions.assertThat(messageServiceV2.getMessagesByChannel(ch).size()).isEqualTo(1);
  }
}
