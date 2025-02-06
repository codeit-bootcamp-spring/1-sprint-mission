package repository;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.BINARY_CONTENT_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DiscodeitApplication.class)
public class FileBinaryContentRepositoryTest {

  private final BinaryContentRepository repository;
  private BinaryContent content1;
  private BinaryContent content2;

  @Autowired
  public FileBinaryContentRepositoryTest(BinaryContentRepository repository){
    this.repository = repository;
  }

  @BeforeEach
  void setUp(){

    File file = new File(BINARY_CONTENT_FILE);

    repository.clear();
    if(file.exists()){
      assertTrue(file.delete(), "failed to delete file");
    }

    content1 = new BinaryContent.BinaryContentBuilder("user1", "file1.png", FileType.JPG, 1024, new byte[]{1, 2, 3})
        .messageId("msg1")
        .channelId("channel1")
        .build();

    content2 = new BinaryContent.BinaryContentBuilder("user2", "file2.mp4", FileType.MP4, 2048, new byte[]{4, 5, 6})
        .messageId("msg2")
        .channelId("channel2")
        .build();
  }

  @Test
  void testSaveAndFind() {

    repository.save(content1);

    Optional<BinaryContent> result = repository.findById(content1.getUUID());

    assertTrue(result.isPresent());
    assertEquals(content1.getUUID(), result.get().getUUID());
  }

  @Test
  void testSaveMultipleAndFindAll(){
    repository.saveMultipleBinaryContent(List.of(content1, content2));

    List<BinaryContent> contents = repository.findAll();

    Assertions.assertThat(contents).hasSize(2);
    Assertions.assertThat(contents).contains(content1, content2);
  }

  @Test
  void saveAndFindById(){
    repository.save(content1);

    Optional<BinaryContent> content = repository.findById(content1.getUUID());

    assertTrue(content.isPresent());
    Assertions.assertThat(content.get()).isEqualTo(content1);
  }

  @Test
  void saveAndFindByChannelId(){
    repository.save(content1);

    List<BinaryContent> contents = repository.findByChannel(content1.getChannelId());
    List<BinaryContent> contents2 = repository.findByChannel(content2.getChannelId());

    Assertions.assertThat(contents).hasSize(1);
    Assertions.assertThat(contents.get(0)).isEqualTo(content1);
    Assertions.assertThat(contents2).isEmpty();
  }

  @Test
  void saveAndFindByMessageId(){
    repository.save(content1);

    List<BinaryContent> contents = repository.findByMessageId(content1.getMessageId());
    List<BinaryContent> contents2 = repository.findByMessageId(content2.getMessageId());

    Assertions.assertThat(contents).hasSize(1);
    Assertions.assertThat(contents.get(0)).isEqualTo(content1);
    Assertions.assertThat(contents2).isEmpty();
  }

  @Test
  void deleteByUserId(){
    repository.save(content1);

    Assertions.assertThat(repository.findById(content1.getUUID()).get()).isEqualTo(content1);

    repository.deleteByUserId(content1.getUserId());

    Assertions.assertThat(repository.findById(content1.getUUID())).isEmpty();
  }

  @Test
  void deleteByMessageId(){
    repository.save(content1);

    Assertions.assertThat(repository.findByMessageId(content1.getMessageId()).get(0)).isEqualTo(content1);

    repository.deleteByUserId(content1.getMessageId());

    Assertions.assertThat(repository.findByMessageId(content1.getMessageId()).isEmpty());
  }

  @Test
  void deleteById(){
    repository.save(content1);

    Assertions.assertThat(repository.findById(content1.getUUID()).get()).isEqualTo(content1);

    repository.deleteById(content1.getUUID());

    Assertions.assertThat(repository.findById(content1.getUUID())).isEmpty();
  }

}
