package repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MessageRepositoryTest {

  private final MessageRepository repository = FileMessageRepository.getInstance();

  @BeforeEach
  void beforeEach(){
    repository.clear();
  }

  @Test
  void saveAndRead(){

    Message message = new Message.MessageBuilder("exampleUUID","exampleUUID", "example content" ).build();
    repository.create(message);

    Message message2 = repository.findById(message.getUUID()).get();

    Assertions.assertThat(message).isEqualTo(message2);
  }

  @Test
  void readByChannel(){
    Message message = new Message.MessageBuilder("exampleUUID","exampleUUID", "example content" ).build();
    Message message2 = new Message.MessageBuilder("exampleUUID","exampleUUID", "example content" ).build();
    Message message3 = new Message.MessageBuilder("exampleUUID","exampleUUID2", "example content" ).build();

    repository.create(message);
    repository.create(message2);
    repository.create(message3);

    List<Message> channel1Messages = repository.findByChannel("exampleUUID");
    List<Message> channel2Messages = repository.findByChannel("exampleUUID2");
    List<Message> allMessages = repository.findAll();

    Assertions.assertThat(channel1Messages.size()).isEqualTo(2);
    Assertions.assertThat(channel2Messages.size()).isEqualTo(1);
    Assertions.assertThat(allMessages).hasSize(3);

  }

  @Test
  void updateMessage(){
    Message message = new Message.MessageBuilder("exampleUUID","exampleUUID", "example content" ).build();
    repository.create(message);

    Message updateMessage = repository.findById(message.getUUID()).get();
    updateMessage.setContent("updated");
    repository.update(updateMessage);

    Assertions.assertThat(repository.findAll()).hasSize(1);
    Assertions.assertThat(repository.findById(message.getUUID()).get().getContent()).isEqualTo("updated");
  }

  @Test
  void delete(){
    Message message = new Message.MessageBuilder("exampleUUID","exampleUUID", "example content" ).build();
    repository.create(message);

    repository.delete(message.getUUID());

    Assertions.assertThat(repository.findAll()).hasSize(0);
  }


}
