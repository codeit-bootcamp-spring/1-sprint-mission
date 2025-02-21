package repository.jcf;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class JCFUserRepositoryTest {


  private JCFUserRepository repository;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  void setUp(){
    repository = new JCFUserRepository();
    user1 = new User.UserBuilder("username1" , "pwd1" , "email1@email.com", "01012345671")
        .nickname("nickname1")
        .build();
    user2 = new User.UserBuilder("username2" , "pwd2" , "email2@email.com", "01012345672")
        .nickname("nickname2")
        .build();
    user3 = new User.UserBuilder("username3" , "pwd3" , "email3@email.com", "010123456783")
        .nickname("nickname3")
        .build();
  }

  @Test
  void testCreateAndFindById(){
    repository.create(user1);

    Optional<User> user = repository.findById(user1.getUUID());
    assertThat(user).isPresent();
    assertThat(user.get().getUUID()).isEqualTo(user1.getUUID());
  }


  @Test
  void testFindAll(){
    repository.create(user1);
    repository.create(user2);
    repository.create(user3);

    List<User> users = repository.findAll();
    assertThat(users).hasSize(3);
    assertThat(users).contains(user1, user2, user3);
  }

  @Test
  void testUpdate(){
    repository.create(user1);
    user1.setUsername("updatedUsername");

    repository.update(user1);

    Optional<User> opUser = repository.findById(user1.getUUID());
    assertThat(opUser).isPresent();

    User user = opUser.get();

    assertThat(user.getUsername()).isEqualTo("updatedUsername");
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void testDelete(){
    repository.create(user1);
    assertThat(repository.findAll()).hasSize(1);

    repository.delete(user1.getUUID());
    assertThat(repository.findAll()).hasSize(0);
  }
}
