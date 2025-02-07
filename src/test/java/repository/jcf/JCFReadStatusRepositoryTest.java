package repository.jcf;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class JCFReadStatusRepositoryTest {

  @Autowired
  private JCFReadStatusRepository repository;

  private ReadStatus readStatus1;
  private ReadStatus readStatus2;
  private ReadStatus readStatus3;

  @BeforeEach
  void setUp(){
    repository.clear();

    readStatus1 = new ReadStatus("channel1", "user1");
    readStatus2 = new ReadStatus("channel1", "user2");
    readStatus3 = new ReadStatus("channel2", "user1");
  }

  @Test
  void testSaveAndFindById(){
    repository.save(readStatus1);

    Optional<ReadStatus> status = repository.findById(readStatus1.getUUID());
    assertThat(status).isPresent();
    assertThat(status.get()).isEqualTo(readStatus1);
  }

  @Test
  void testUpdate() throws InterruptedException {
    repository.save(readStatus1);

    Instant readAt = readStatus1.getLastReadAt();

    TimeUnit.MILLISECONDS.sleep(50);

    readStatus1.updateLastReadAt();

    repository.save(readStatus1);
    Optional<ReadStatus> status = repository.findById(readStatus1.getUUID());

    assertThat(status).isPresent();

    assertThat(status.get().getLastReadAt()).isNotEqualTo(readAt);
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void testFindByUserId(){
    repository.save(readStatus1);
    repository.save(readStatus2);
    repository.save(readStatus3);
    List<ReadStatus> status = repository.findByUserId(readStatus1.getUserId());

    assertThat(status).hasSize(2);
  }

  @Test
  void testFindByChannelId(){
    repository.save(readStatus1);
    repository.save(readStatus2);
    repository.save(readStatus3);

    List<ReadStatus> status = repository.findByChannelId(readStatus1.getChannelId());

    assertThat(status).hasSize(2);
  }

  @Test
  void testFindByChannelIdAndUserId(){
    repository.save(readStatus1);

    Optional<ReadStatus> optionalReadStatus = repository.findByChannelIdAndUserId(readStatus1.getChannelId(), readStatus1.getUserId());
    assertThat(optionalReadStatus).isPresent();
    assertThat(optionalReadStatus.get()).isEqualTo(readStatus1);
  }

  @Test
  void testFindAll(){
    repository.save(readStatus1);
    repository.save(readStatus2);

    List<ReadStatus> statuses = repository.findAll();
    assertThat(statuses).hasSize(2);
  }

  @Test
  void testDeleteByUserId(){
    repository.save(readStatus1);
    repository.save(readStatus2);
    repository.save(readStatus3);
    repository.deleteByUserId(readStatus1.getUserId());

    List<ReadStatus> statuses = repository.findAll();
    assertThat(statuses).hasSize(1);
  }

  @Test
  void testDeleteByChannelId(){
    repository.save(readStatus1);
    repository.save(readStatus2);
    repository.save(readStatus3);
    repository.deleteByChannelId(readStatus1.getChannelId());

    List<ReadStatus> statuses = repository.findAll();
    assertThat(statuses).hasSize(1);
  }

}
