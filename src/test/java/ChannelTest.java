import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;
import discodeit.service.jcf.JCFServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ChannelTest {

    private ServiceFactory serviceFactory;
    private UserService jcfUserService;
    private ChannelService jcfChannelService;

    @BeforeEach
    public void setUp() {
        serviceFactory = JCFServiceFactory.getInstance();
        jcfUserService = serviceFactory.getUserService();
        jcfChannelService = serviceFactory.getChannelService();
    }

    @Test
    void 채널에_유저가_중복_참여() {
        User user1 = jcfUserService.createUser("name", "email1@codeit.com", "010-1111-1111", "1234");
        User user2 = jcfUserService.createUser("name", "email2@codeit.com", "010-2222-2222", "1234");
        Channel channel1 = jcfChannelService.createChannel("channel", "introduction", user1);
        jcfChannelService.updateParticipants(channel1, user2);

        assertThatThrownBy(() -> jcfChannelService.updateParticipants(channel1, user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 가입된 유저입니다.");
    }

    @Test
    void 방장이_아닌_유저가_채널_삭제() {
        ServiceFactory serviceFactory = JCFServiceFactory.getInstance();
        UserService jcfUserService = serviceFactory.getUserService();
        ChannelService jcfChannelService = serviceFactory.getChannelService();

        User user1 = jcfUserService.createUser("name", "email3@codeit.com", "010-3333-3333", "1234");
        User user2 = jcfUserService.createUser("name", "email4@codeit.com", "010-4444-4444", "1234");
        Channel channel1 = jcfChannelService.createChannel("channel", "introduction", user1);
        jcfChannelService.updateParticipants(channel1, user2);

        assertThatThrownBy(() -> jcfChannelService.deleteChannel(channel1, user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("채널 삭제는 방장만 가능합니다.");
    }
}
