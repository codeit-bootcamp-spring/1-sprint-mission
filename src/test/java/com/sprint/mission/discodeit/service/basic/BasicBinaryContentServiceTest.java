package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicBinaryContentServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(BasicBinaryContentServiceTest.class);

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentMapper binaryContentMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @InjectMocks
  private BasicBinaryContentService binaryContentService;

  private UUID binaryContentId;
  private String fileName;
  private String contentType;
  private byte[] bytes;
  private BinaryContent binaryContent;
  private BinaryContentDto binaryContentDto;

  @BeforeEach
  void setUp() {
    binaryContentId = UUID.randomUUID();
    fileName = "test.jpg";
    contentType = "image/jpeg";
    bytes = "test data".getBytes();

    binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
    ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);

    binaryContentDto = new BinaryContentDto(
        binaryContentId,
        fileName,
        (long) bytes.length,
        contentType
    );
  }

  @Test
  @Order(1)
  @DisplayName("바이너리 콘텐츠 생성 성공")
  void createBinaryContent_Success() {
    logger.info("==== 바이너리 콘텐츠 생성 성공 테스트 시작 ====");

    // given
    BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType,
        bytes);

    given(binaryContentRepository.save(any(BinaryContent.class))).will(invocation -> {
      BinaryContent binaryContent = invocation.getArgument(0);
      ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);
      return binaryContent;
    });
    given(binaryContentMapper.toDto(any(BinaryContent.class))).willReturn(binaryContentDto);

    // when
    BinaryContentDto result = binaryContentService.create(request);

    // then
    assertThat(result).isEqualTo(binaryContentDto);
    verify(binaryContentRepository).save(any(BinaryContent.class));
    verify(binaryContentStorage).put(binaryContentId, bytes);

    logger.info("==== 바이너리 콘텐츠 생성 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(2)
  @DisplayName("바이너리 콘텐츠 조회 성공")
  void findBinaryContent_Success() {
    logger.info("==== 바이너리 콘텐츠 조회 성공 테스트 시작 ====");

    // given
    given(binaryContentRepository.findById(eq(binaryContentId))).willReturn(
        Optional.of(binaryContent));
    given(binaryContentMapper.toDto(eq(binaryContent))).willReturn(binaryContentDto);

    // when
    BinaryContentDto result = binaryContentService.find(binaryContentId);

    // then
    assertThat(result).isEqualTo(binaryContentDto);

    logger.info("==== 바이너리 콘텐츠 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(3)
  @DisplayName("존재하지 않는 바이너리 콘텐츠 조회 시 예외 발생")
  void findBinaryContent_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 바이너리 콘텐츠 조회 시 예외 발생 테스트 시작 ====");

    // given
    given(binaryContentRepository.findById(eq(binaryContentId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> binaryContentService.find(binaryContentId))
        .isInstanceOf(BinaryContentNotFoundException.class);

    logger.info("==== 존재하지 않는 바이너리 콘텐츠 조회 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(4)
  @DisplayName("여러 ID로 바이너리 콘텐츠 목록 조회 성공")
  void findAllByIdIn_Success() {
    logger.info("==== 여러 ID로 바이너리 콘텐츠 목록 조회 성공 테스트 시작 ====");

    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<UUID> ids = Arrays.asList(id1, id2);

    BinaryContent content1 = new BinaryContent("file1.jpg", 100L, "image/jpeg");
    ReflectionTestUtils.setField(content1, "id", id1);

    BinaryContent content2 = new BinaryContent("file2.jpg", 200L, "image/png");
    ReflectionTestUtils.setField(content2, "id", id2);

    List<BinaryContent> contents = Arrays.asList(content1, content2);

    BinaryContentDto dto1 = new BinaryContentDto(id1, "file1.jpg", 100L, "image/jpeg");
    BinaryContentDto dto2 = new BinaryContentDto(id2, "file2.jpg", 200L, "image/png");

    given(binaryContentRepository.findAllById(eq(ids))).willReturn(contents);
    given(binaryContentMapper.toDto(eq(content1))).willReturn(dto1);
    given(binaryContentMapper.toDto(eq(content2))).willReturn(dto2);

    // when
    List<BinaryContentDto> result = binaryContentService.findAllByIdIn(ids);

    // then
    assertThat(result).containsExactly(dto1, dto2);

    logger.info("==== 여러 ID로 바이너리 콘텐츠 목록 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(5)
  @DisplayName("바이너리 콘텐츠 삭제 성공")
  void deleteBinaryContent_Success() {
    logger.info("==== 바이너리 콘텐츠 삭제 성공 테스트 시작 ====");

    // given
    given(binaryContentRepository.existsById(binaryContentId)).willReturn(true);

    // when
    binaryContentService.delete(binaryContentId);

    // then
    verify(binaryContentRepository).deleteById(binaryContentId);

    logger.info("==== 바이너리 콘텐츠 삭제 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(6)
  @DisplayName("존재하지 않는 바이너리 콘텐츠 삭제 시 예외 발생")
  void deleteBinaryContent_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 바이너리 콘텐츠 삭제 시 예외 발생 테스트 시작 ====");

    // given
    given(binaryContentRepository.existsById(eq(binaryContentId))).willReturn(false);

    // when & then
    assertThatThrownBy(() -> binaryContentService.delete(binaryContentId))
        .isInstanceOf(BinaryContentNotFoundException.class);

    logger.info("==== 존재하지 않는 바이너리 콘텐츠 삭제 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }
}