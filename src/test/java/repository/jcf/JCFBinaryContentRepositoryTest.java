package repository.jcf;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.util.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class JCFBinaryContentRepositoryTest {


  private JCFBinaryContentRepository repository;
  private BinaryContent binaryContent1;
  private BinaryContent binaryContent2;
  private BinaryContent binaryContent3;

  @BeforeEach
  void setUp(){
    repository = new JCFBinaryContentRepository();
    repository.clear();
    binaryContent1 = new BinaryContent.BinaryContentBuilder("user1", "file1", "FileType.JPG", 10, new byte[]{1,1,1,1}).build();

    binaryContent2 = new BinaryContent.BinaryContentBuilder("user1", "file1", "FileType.JPG", 10, new byte[]{1,1,1,1})
        .channelId("channel1")
        .messageId("message1")
        .build();

    binaryContent3 = new BinaryContent.BinaryContentBuilder("user2", "file2", "FileType.JPG", 10, new byte[]{1,1,1,1})
        .channelId("channel2")
        .messageId("message2")
        .build();

    repository.save(binaryContent1);
    repository.save(binaryContent2);
    repository.save(binaryContent3);
  }

  @Test
  void testSave(){
    BinaryContent newContent = new BinaryContent.BinaryContentBuilder("user1", "file1", "FileType.JPG", 10, new byte[]{1,1,1,1}).build();
    repository.save(newContent);

    Optional<BinaryContent> saved = repository.findById(newContent.getUUID());
    assertThat(saved).isPresent();
  }

  @Test
  void testSaveMultipleBinaryContentAndFindAll(){
    BinaryContent newContent1 = new BinaryContent.BinaryContentBuilder("user1", "file1", "FileType.JPG", 10, new byte[]{1,1,1,1}).build();
    BinaryContent newContent2 = new BinaryContent.BinaryContentBuilder("user1", "file1", "FileType.JPG", 10, new byte[]{1,1,1,1}).build();
    BinaryContent newContent3 = new BinaryContent.BinaryContentBuilder("user1", "file1", "FileType.JPG", 10, new byte[]{1,1,1,1}).build();

    repository.saveMultipleBinaryContent(List.of(newContent1, newContent2, newContent3));

    List<BinaryContent> contents = repository.findAll();
    assertThat(contents).hasSize(6);
    assertThat(contents).contains(binaryContent1, binaryContent2, binaryContent3, newContent1, newContent2, newContent3);
  }

  @Test
  void testFindByUserId(){
    List<BinaryContent> content = repository.findByUserId("user1");

    assertThat(content).hasSize(2);
    assertThat(content).contains(binaryContent1, binaryContent2);
  }

  @Test
  void testFindByChannel(){
    List<BinaryContent> contents = repository.findByChannel("channel1");

      assertThat(contents).hasSize(1);
      assertThat(contents).containsExactly(binaryContent2);
  }

  @Test
  void testFindByMessageId(){
    List<BinaryContent> contents = repository.findByMessageId("message1");

    assertThat(contents).hasSize(1);
    assertThat(contents).containsExactly(binaryContent2);
  }

  @Test
  void testDeleteByUserId(){
    repository.deleteByUserId("user1");
    List<BinaryContent> contents = repository.findAll();

    assertThat(contents).hasSize(1);
    assertThat(contents).containsExactly(binaryContent3);
  }

  @Test
  void testDeleteByMessageId(){
    repository.deleteByMessageId("message1");
    List<BinaryContent> contents = repository.findAll();

    assertThat(contents).hasSize(2);
    assertThat(contents).contains(binaryContent1, binaryContent3);
  }

  @Test
  void testDeleteById(){
    repository.deleteById(binaryContent1.getUUID());
    List<BinaryContent> contents = repository.findAll();

    assertThat(contents).hasSize(2);
    assertThat(contents).contains(binaryContent2, binaryContent3);
    assertThat(contents).doesNotContain(binaryContent1);
  }
}
