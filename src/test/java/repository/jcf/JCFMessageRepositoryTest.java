package repository.jcf;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class JCFMessageRepositoryTest {

  @Autowired
  private JCFMessageRepository repository;
  private Message message1;
  private Message message2;
  private Message message3;

  @BeforeEach
  void setUp() throws InterruptedException {
    repository.clear();
    message1 = new Message.MessageBuilder("user1", "channel1", "Hello World!").build();
    TimeUnit.MILLISECONDS.sleep(5);
    message2 = new Message.MessageBuilder("user2", "channel1", "Second message").build();
    message3 = new Message.MessageBuilder("user3", "channel2", "Different channel").build();
    repository.create(message1);



    repository.create(message2);

    repository.create(message3);
  }

  @Test
  void testCreateAndFindById() {
    Optional<Message> foundMessage = repository.findById(message1.getUUID());
    assertTrue(foundMessage.isPresent());
    assertEquals("Hello World!", foundMessage.get().getContent());
  }

  @Test
  void testFindByChannel() {
    List<Message> channelMessages = repository.findByChannel("channel1");
    assertEquals(2, channelMessages.size());
  }

  @Test
  void testFindAll() {
    List<Message> allMessages = repository.findAll();
    assertEquals(3, allMessages.size());
  }

  @Test
  void testUpdate() {
    message1.setContent("Updated content");
    repository.update(message1);
    Optional<Message> updatedMessage = repository.findById(message1.getUUID());
    assertTrue(updatedMessage.isPresent());
    assertEquals("Updated content", updatedMessage.get().getContent());
  }

  @Test
  void testDelete() {
    repository.delete(message1.getUUID());
    Optional<Message> deletedMessage = repository.findById(message1.getUUID());
    assertFalse(deletedMessage.isPresent());
  }

  @Test
  void testDeleteByChannel() {
    repository.deleteByChannel("channel1");
    List<Message> remainingMessages = repository.findAll();
    assertEquals(1, remainingMessages.size());
    assertEquals("Different channel", remainingMessages.get(0).getContent());
  }

  @Test
  void testFindLatestChannelMessage() {
    Message latestMessage = repository.findLatestChannelMessage("channel1");
    assertNotNull(latestMessage);
    assertEquals("Second message", latestMessage.getContent());
  }



}
