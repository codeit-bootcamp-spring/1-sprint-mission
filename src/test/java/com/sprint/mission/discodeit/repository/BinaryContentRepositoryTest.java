package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestAuditingConfig.class)
public class BinaryContentRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(BinaryContentRepositoryTest.class);

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  private BinaryContent createTestBinaryContent(String fileName, Long size, String contentType) {
    BinaryContent binaryContent = new BinaryContent(fileName, size, contentType);
    BinaryContent savedContent = binaryContentRepository.save(binaryContent);
    entityManager.flush();
    return savedContent;
  }

  @Test
  @Order(1)
  @DisplayName("바이너리 콘텐츠를 저장하고 ID로 조회할 수 있다")
  void saveAndFindById() {
    log.info("======== 바이너리 콘텐츠 저장 및 ID로 조회 테스트 시작 ========");

    String fileName = "test-image.jpg";
    Long size = 1024L;
    String contentType = "image/jpeg";

    BinaryContent savedContent = createTestBinaryContent(fileName, size, contentType);

    Optional<BinaryContent> foundContent = binaryContentRepository.findById(savedContent.getId());
    assertThat(foundContent).isPresent();
    assertThat(foundContent.get().getFileName()).isEqualTo(fileName);
    assertThat(foundContent.get().getSize()).isEqualTo(size);
    assertThat(foundContent.get().getContentType()).isEqualTo(contentType);

    log.info("======== 바이너리 콘텐츠 저장 및 ID로 조회 테스트 종료 ========");
  }

  @Test
  @Order(2)
  @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
  void findByIdWithNonExistentIdReturnsEmpty() {
    log.info("======== 존재하지 않는 ID로 바이너리 콘텐츠 조회 테스트 시작 ========");

    UUID nonExistentId = UUID.randomUUID();

    Optional<BinaryContent> foundContent = binaryContentRepository.findById(nonExistentId);

    assertThat(foundContent).isEmpty();

    log.info("======== 존재하지 않는 ID로 바이너리 콘텐츠 조회 테스트 종료 ========");
  }

  @Test
  @Order(3)
  @DisplayName("저장된 모든 바이너리 콘텐츠를 조회할 수 있다")
  void findAllBinaryContents() {
    log.info("======== 모든 바이너리 콘텐츠 조회 테스트 시작 ========");

    createTestBinaryContent("image1.jpg", 1024L, "image/jpeg");
    createTestBinaryContent("image2.png", 2048L, "image/png");
    createTestBinaryContent("document.pdf", 4096L, "application/pdf");

    List<BinaryContent> contentList = binaryContentRepository.findAll();

    assertThat(contentList).hasSize(3);
    assertThat(contentList).extracting("fileName")
        .containsExactlyInAnyOrder("image1.jpg", "image2.png", "document.pdf");
    assertThat(contentList).extracting("size").containsExactlyInAnyOrder(1024L, 2048L, 4096L);

    log.info("======== 모든 바이너리 콘텐츠 조회 테스트 종료 ========");
  }

  @Test
  @Order(4)
  @DisplayName("바이너리 콘텐츠를 삭제하면 더 이상 조회되지 않는다")
  void deleteBinaryContentById() {
    log.info("======== 바이너리 콘텐츠 삭제 테스트 시작 ========");

    BinaryContent content = createTestBinaryContent("delete-me.jpg", 1024L, "image/jpeg");

    assertThat(binaryContentRepository.findById(content.getId())).isPresent();

    binaryContentRepository.deleteById(content.getId());
    entityManager.flush();
    entityManager.clear();

    assertThat(binaryContentRepository.findById(content.getId())).isEmpty();

    log.info("======== 바이너리 콘텐츠 삭제 테스트 종료 ========");
  }

  @Test
  @Order(5)
  @DisplayName("기존 콘텐츠를 삭제하고 새로운 콘텐츠로 교체할 수 있다")
  void updateBinaryContentWithNewEntity() {
    log.info("======== 바이너리 콘텐츠 새로 생성하여 업데이트 테스트 시작 ========");

    BinaryContent oldContent = createTestBinaryContent("original.jpg", 1024L, "image/jpeg");
    UUID oldContentId = oldContent.getId();

    binaryContentRepository.deleteById(oldContentId);
    BinaryContent newContent = createTestBinaryContent("updated.jpg", 2048L, "image/jpeg");

    entityManager.flush();
    entityManager.clear();

    Optional<BinaryContent> deletedContent = binaryContentRepository.findById(oldContentId);
    assertThat(deletedContent).isEmpty();

    Optional<BinaryContent> updatedContent = binaryContentRepository.findById(newContent.getId());
    assertThat(updatedContent).isPresent();
    assertThat(updatedContent.get().getFileName()).isEqualTo("updated.jpg");
    assertThat(updatedContent.get().getSize()).isEqualTo(2048L);
    assertThat(updatedContent.get().getContentType()).isEqualTo("image/jpeg");

    log.info("======== 바이너리 콘텐츠 새로 생성하여 업데이트 테스트 종료 ========");
  }
}
