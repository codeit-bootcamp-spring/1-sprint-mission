package com.sprint.mission.discodeit.service.binarycontent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binary.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BinaryContentServiceImplTest {

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @InjectMocks
  private BinaryContentServiceImpl binaryContentService;

  @Test
  @DisplayName("파일 찾기 - 성공")
  void findFile() throws Exception {
    UUID fileId = UUID.randomUUID();
    BinaryContent binaryContent = new BinaryContent("test.json", 1024L, "application/json");

    when(binaryContentRepository.findById(any(UUID.class))).thenReturn(Optional.of(binaryContent));

    BinaryContentResponse response = binaryContentService.find(fileId);

    assertThat(response).isNotNull();
    assertThat("test.json").isEqualTo(response.fileName());

    verify(binaryContentRepository).findById(any(UUID.class));
  }

  @Test
  @DisplayName("파일 찾기 - 실패")
  void findFileFail() throws Exception {
    UUID fileId = UUID.randomUUID();

    when(binaryContentRepository.findById(fileId)).thenReturn(Optional.empty());

    assertThrows(BinaryContentNotFoundException.class, () -> binaryContentService.find(fileId));
  }

  @Test
  @DisplayName("모든 파일 찾기")
  void findAll() {

    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<BinaryContentResponse> testBinaryContentList = List.of(
        new BinaryContentResponse(id1, "test1.json", 12L, "application/json"),
        new BinaryContentResponse(id2, "test2.json", 20L, "application/json")
    );
    List<String> testBinaryContentIds = List.of(id1.toString(), id2.toString());

    when(binaryContentRepository.findAllByIdIn(anyList()))
        .thenReturn(testBinaryContentList);

    List<BinaryContentResponse> responses = binaryContentService.findAll(testBinaryContentIds);

    assertNotNull(responses);
    assertEquals(2, responses.size());
    assertEquals("test1.json", responses.get(0).fileName());
    assertEquals("test2.json", responses.get(1).fileName());

    verify(binaryContentRepository, times(1)).findAllByIdIn(anyList());
  }

  @Test
  @DisplayName("파일 다운로드 - 성공")
  void downloadFile() {
    UUID testId = UUID.randomUUID();
    BinaryContent binaryContent = new BinaryContent("test1.json", 12L, "application/json");

    when(binaryContentRepository.findById(testId))
        .thenReturn(Optional.of(binaryContent));

    ResponseEntity<byte[]> mockResponse = ResponseEntity.ok().build();
    doReturn(mockResponse).when(binaryContentStorage).download(any());

    ResponseEntity<?> response = binaryContentService.downloadBinaryContent(testId);

    assertNotNull(response);
    verify(binaryContentRepository, times(1)).findById(testId);
    verify(binaryContentStorage, times(1)).download(any());
  }

  @Test
  @DisplayName("파일 다운로드 - 실패")
  void downloadBinaryContent_WhenFileDoesNotExist_ShouldThrowException() {
    UUID uuid = UUID.randomUUID();
    when(binaryContentRepository.findById(uuid))
        .thenReturn(Optional.empty());

    BinaryContentNotFoundException exception = assertThrows(
        BinaryContentNotFoundException.class,
        () -> binaryContentService.downloadBinaryContent(uuid));

    assertEquals(ErrorCode.BINARY_CONTENT_NOT_FOUND, exception.getErrorCode());
    assertTrue(exception.getDetails().containsKey(uuid.toString()));

    verify(binaryContentRepository, times(1)).findById(uuid);
    verify(binaryContentStorage, never()).download(any());
  }
}
