package repository;

import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChannelRepositoryTest {
  private final ChannelRepository repository = FileChannelRepository.getInstance();

  @BeforeEach
  void beforeEach(){
    repository.clear();
  }

  @Test
  void saveAndRead(){
    ChatChannel chatChannel = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleUUID")
        .categoryUUID("exampleUUID")
        .build();
    repository.save(chatChannel);

    BaseChannel channel = repository.findById(chatChannel.getUUID()).get();

    Assertions.assertThat(channel).isInstanceOf(ChatChannel.class);
    Assertions.assertThat(channel).isEqualTo(chatChannel);
  }

  @Test
  void updateChannel(){
    ChatChannel chatChannel = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleUUID")
        .categoryUUID("exampleUUID")
        .build();
    repository.save(chatChannel);

    ChatChannel voiceChannel = new ChatChannel.ChatChannelBuilder("voice")
        .serverUUID("exampleUUID")
        .categoryUUID("exampleUUID")
        .build();
    repository.save(voiceChannel);
    BaseChannel updateChannel = repository.findById(chatChannel.getUUID()).get();
    updateChannel.setChannelName("UPDATED");

    BaseChannel updatedChannel = repository.update(updateChannel);

    Assertions.assertThat(updatedChannel).isInstanceOf(ChatChannel.class);
    Assertions.assertThat(updatedChannel).isNotInstanceOf(VoiceChannel.class);
    Assertions.assertThat(chatChannel).isEqualTo(updatedChannel);
    Assertions.assertThat(updatedChannel.getChannelName()).isEqualTo("UPDATED");
    Assertions.assertThat(repository.findById(voiceChannel.getUUID()).getClass()).isNotEqualTo(updatedChannel.getClass());
  }

  @Test
  void deleteChannel(){
    ChatChannel voiceChannel = new ChatChannel.ChatChannelBuilder("voice")
        .serverUUID("exampleUUID")
        .categoryUUID("exampleUUID")
        .build();
    repository.save(voiceChannel);
    repository.delete(voiceChannel.getUUID());

    Assertions.assertThat(repository.findAll().size()).isEqualTo(0);
  }
}
