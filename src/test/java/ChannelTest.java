import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChatBehavior;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ChannelTest {

    private final JCFChannelService instance = JCFChannelService.getInstance();

    @DisplayName("채널을 생성할수 있다")
    @Test
    void 채널을_생성할수_있다(){
        Channel channel = JCFChannelService.getInstance().createChannel(
            new Channel(
                "server-uuid-1",
                "category-uuid-1",
                "General",
                100,
                false,
                null,
                new ChatBehavior(JCFUserService.getInstance(), JCFMessageService.getInstance())
            )
        );
        Assertions.assertThat(channel.getCategoryUUID()).isNotEmpty();
    }

    @DisplayName("채널을 저장하고 불러올수 있다")
    @Test
    void 채널을_저장하고_불러올수_있다(){
        Channel channel = JCFChannelService.getInstance().createChannel(
            new Channel(
                "server-uuid-1",
                "category-uuid-1",
                "General",
                100,
                false,
                null,
                new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
            )
        );

        String channelId = channel.getUUID();
        JCFChannelService.getInstance().createChannel(channel);
        Assertions.assertThat(channel).isEqualTo(
            JCFChannelService.getInstance().getChannelById(channelId).get()
        );
    }

    @DisplayName("채널정보를 업데이트 할 수있다")
    @Test
    void 채널정보를_업데이트_할수_있다(){

        Channel channel = instance.createChannel(
            new Channel(
                "server-uuid-1",
                "category-uuid-1",
                "General",
                100,
                false,
                null,
                new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
            )
        );

        String channelId = channel.getUUID();

        ChannelUpdateDto updateChannel = new ChannelUpdateDto(
            Optional.of("UpdatedName"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );

        instance.updateChannel(channelId, updateChannel);
        Assertions.assertThat(channel).isSameAs(instance.getChannelById(channelId).get());
    }

    @DisplayName("채널을 삭제할 수 있다")
    @Test
    void 채널을_삭제할수_있다(){
        Channel channel = instance.createChannel(
            new Channel(
                "server-uuid-1",
                "category-uuid-1",
                "General",
                100,
                false,
                null,
                new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
            )
        );

        instance.deleteChannel(channel.getUUID());

        Assertions.assertThat(instance.getChannelById(channel.getUUID())).isEmpty();
    }


}
