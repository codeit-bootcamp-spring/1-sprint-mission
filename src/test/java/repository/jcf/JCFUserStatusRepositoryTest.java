package repository.jcf;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class JCFUserStatusRepositoryTest {

  @Autowired
  private JCFUserStatusRepository repository;
  private UserStatus userStatus1;
  private UserStatus userStatus2;
  private UserStatus userStatus3;

  @BeforeEach
  void setUp(){
    repository.clear();

    userStatus1 = new UserStatus("user1" , Instant.now());
    userStatus2 = new UserStatus("user2", Instant.now());
    userStatus3 = new UserStatus("user3", Instant.now());
  }

  @Test
  void testSaveAndFindById(){
    repository.save(userStatus1);

    Optional<UserStatus> optionalStatus = repository.findById(userStatus1.getUUID());

    assertThat(optionalStatus).isPresent();
    UserStatus status = optionalStatus.get();

    assertThat(status.getUUID()).isEqualTo(userStatus1.getUUID());
  }

  @Test
  void testFindByUserId(){
    repository.save(userStatus1);

    Optional<UserStatus> optional = repository.findByUserId(userStatus1.getUserId());
    assertThat(optional).isPresent();

    UserStatus status = optional.get();

    assertThat(status.getUUID()).isEqualTo(userStatus1.getUUID());
  }

  @Test
  void testFindAll(){
    repository.save(userStatus1);
    repository.save(userStatus2);

    List<UserStatus> statuses = repository.findAll();

    assertThat(statuses).hasSize(2);
    assertThat(statuses).contains(userStatus1, userStatus2);
  }

  @Test
  void testDeleteByUserIdAndById(){
    repository.save(userStatus1);
    repository.save(userStatus2);

    repository.deleteByUserId(userStatus1.getUserId());

    assertThat(repository.findAll()).hasSize(1);

    repository.deleteById(userStatus2.getUUID());

    assertThat(repository.findAll()).hasSize(0);
  }

}
