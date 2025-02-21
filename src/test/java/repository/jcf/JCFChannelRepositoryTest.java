package repository.jcf;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class JCFChannelRepositoryTest {


  private JCFChannelRepository repository;
  private Channel channel1;
  private Channel channel2;
  private Channel channel3;

  @BeforeEach
  void setUp(){
    repository = new JCFChannelRepository();

    channel1 = new Channel.ChannelBuilder("channel1", Channel.ChannelType.CHAT)
        .serverUUID("server1")
        .build();

    channel2 = new Channel.ChannelBuilder("channel2", Channel.ChannelType.VOICE)
        .serverUUID("server1")
        .isPrivate(true)
        .build();

    channel3 = new Channel.ChannelBuilder("channel3", Channel.ChannelType.VOICE)
        .serverUUID("server2")
        .build();
  }

  @Test
  void testSaveAndFindById(){
    repository.save(channel1);

    Optional<Channel> channel = repository.findById(channel1.getUUID());

    assertThat(channel).isPresent();
    assertThat(channel.get().getUUID()).isEqualTo(channel1.getUUID());
    assertThat(channel.get().getChannelName()).isEqualTo("channel1");
  }

  @Test
  void testFindAll(){
    repository.save(channel1);
    repository.save(channel2);
    repository.save(channel3);

    List<Channel> channels = repository.findAll();

    assertThat(channels).hasSize(3);
  }

  @Test
  void testUpdate(){
    repository.save(channel1);

    channel1.setChannelName("updatedChannelName");
    repository.update(channel1);

    Optional<Channel> channel = repository.findById(channel1.getUUID());
    assertThat(channel).isPresent();
    assertThat(channel.get().getChannelName()).isEqualTo("updatedChannelName");

  }

  @Test
  void testDelete(){
    repository.save(channel1);
    assertThat(repository.findById(channel1.getUUID())).isPresent();
    repository.delete(channel1.getUUID());

    assertThat(repository.findById(channel1.getUUID())).isEmpty();
  }
}
