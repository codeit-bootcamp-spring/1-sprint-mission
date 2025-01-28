import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.CHANNEL_FILE;
import static com.sprint.mission.discodeit.constant.FileConstant.USER_FILE;

public class FileChannelServiceTest {

  FileChannelService channelService = FileChannelService.getInstance();

  @BeforeEach
  void setUp() {

    File file = new File(CHANNEL_FILE);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  void saveAndGetFromFile(){
    VoiceChannel vc = new VoiceChannel.VoiceChannelBuilder("voice")
        .serverUUID("exampleServerUUID")
        .categoryUUID("exampleCategoryUUID")
        .build();

    ChatChannel cc = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleServerUUID2")
        .categoryUUID("exampleCategoryUUID2")
        .build();
    channelService.createChannel(vc);
    channelService.createChannel(cc);
    Assertions.assertThat(channelService.getChannelById(vc.getUUID()).get()).isEqualTo(vc);
    Assertions.assertThat(channelService.getChannelById(cc.getUUID()).get()).isEqualTo(cc);
  }

  @Test
  void getAllChannels(){
    VoiceChannel vc = new VoiceChannel.VoiceChannelBuilder("voice")
        .serverUUID("exampleServerUUID")
        .categoryUUID("exampleCategoryUUID")
        .build();
    VoiceChannel vc2 = new VoiceChannel.VoiceChannelBuilder("voice")
        .serverUUID("exampleServerUUID2")
        .categoryUUID("exampleCategoryUUID2")
        .build();

    ChatChannel cc = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleServerUUID3")
        .categoryUUID("exampleCategoryUUID3")
        .build();
    ChatChannel cc2 = new ChatChannel.ChatChannelBuilder("chat2")
        .serverUUID("exampleServerUUID4")
        .categoryUUID("exampleCategoryUUID4")
        .build();

    channelService.createChannel(cc);
    channelService.createChannel(cc2);
    channelService.createChannel(vc);
    channelService.createChannel(vc2);


    List<BaseChannel> channelList = channelService.getAllChannels();

    Assertions.assertThat(channelList).contains(vc,vc2,cc,cc2);
  }

  @Test
  void updateChannel(){


    VoiceChannel vc = (VoiceChannel) channelService.createChannel(new VoiceChannel.VoiceChannelBuilder("voice")
        .serverUUID("exampleServerUUID")
        .categoryUUID("exampleCategoryUUID")
        .build());

    ChannelUpdateDto updateDto = new ChannelUpdateDto(
        Optional.of("updatedName")
        , Optional.empty()
        , Optional.empty()
        , Optional.empty()
    );

    channelService.updateChannel(vc.getUUID(), updateDto);

    Assertions.assertThat(channelService.getChannelById(vc.getUUID()).get().getChannelName()).isEqualTo("updatedName");
    Assertions.assertThat(channelService.getChannelById(vc.getUUID()).get()).isEqualTo(vc);
  }

  @Test
  void deleteChannel(){
    ChatChannel cc = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleServerUUID3")
        .categoryUUID("exampleCategoryUUID3")
        .build();

    channelService.createChannel(cc);


    Assertions.assertThat(channelService.getAllChannels()).hasSize(1);


    channelService.deleteChannel(cc.getUUID());

    Assertions.assertThat(channelService.getAllChannels()).hasSize(0);
  }
}
