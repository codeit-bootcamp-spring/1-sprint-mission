package service;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class BasicChannelServiceTest {

  ChannelRepository channelRepository = FileChannelRepository.getInstance();
  ChannelService channelService = BasicChannelService.getInstance(channelRepository);

  @BeforeEach
  void beforeEach(){
    channelRepository.clear();
  }

  @Test
  void createAndRead(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    channelService.createChannel(channel);


    assertThat(channelService.getChannelById(channel.getUUID()).get()).isEqualTo(channel);
  }

  @Test
  void multipleChannel(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    VoiceChannel channel2 = new VoiceChannel.VoiceChannelBuilder("voiceChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    channelService.createChannel(channel);
    channelService.createChannel(channel2);

    assertThat(channelService.getChannelById(channel2.getUUID()).get()).isInstanceOf(VoiceChannel.class);
    assertThat(channelService.getChannelById(channel.getUUID()).get()).isNotInstanceOf(VoiceChannel.class);
    assertThat(channelService.getAllChannels().size()).isEqualTo(2);
  }

  @Test
  void updateChannel(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    ChannelUpdateDto updateDto = new ChannelUpdateDto(
        Optional.of("updated"),
        Optional.empty(),
        Optional.empty(),
        Optional.empty()
    );
    channelService.createChannel(channel);

    channelService.updateChannel(channel.getUUID(), updateDto);

    BaseChannel chan = channelService.getChannelById(channel.getUUID()).get();
    assertThat(chan).isEqualTo(channel);
    assertThat(chan.getChannelName()).isEqualTo("updated");
  }

  @Test
  void delete(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();
    channelService.createChannel(channel);
    assertThat(channelService.getAllChannels()).hasSize(1);
    channelService.deleteChannel(channel.getUUID());
    assertThat(channelService.getAllChannels()).hasSize(0);
  }

}
