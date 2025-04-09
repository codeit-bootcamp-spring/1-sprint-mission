package com.sprint.mission.unit.binary_content;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.service.basic.BinaryContentServiceImpl;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.unit.TestEntityFactory;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class BinaryContentServiceUnitTest {

  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private MessageAttachmentRepository messageAttachmentRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @InjectMocks
  private BinaryContentServiceImpl binaryContentService;

  private BinaryContent content;

  @BeforeEach
  void setUp() {
    content = TestEntityFactory.createBinaryContent();
  }

  @Test
  void download_success() throws Exception {
    // given
    UUID id = content.getId();
    ByteArrayResource resource = new ByteArrayResource("test".getBytes());
    given(binaryContentStorage.download(id)).willAnswer(invocation -> {
      return ResponseEntity.ok().body(resource);
    });

    // when
    ResponseEntity<Resource> result = binaryContentService.download(id.toString());

    // then
    assertThat(result.getBody()).isEqualTo(resource);
  }

  @Test
  void download_shouldThrow_whenIOException() throws Exception {
    // given
    UUID id = content.getId();
    given(binaryContentStorage.download(id)).willThrow(IOException.class);

    // when & then
    assertThatThrownBy(() -> binaryContentService.download(id.toString()))
        .isInstanceOf(FileException.class)
        .hasMessageContaining(ErrorCode.ERROR_WHILE_DOWNLOADING.getMessage());
  }

  @Test
  void download_shouldThrow_ifNoResource() throws Exception {
    UUID id = content.getId();
    given(binaryContentStorage.download(id)).willAnswer(invocation -> {
      return ResponseEntity.ok().body("NOT A RESOURCE");
    });

    assertThatThrownBy(() -> binaryContentService.download(id.toString()))
        .isInstanceOf(FileException.class)
        .hasMessageContaining(ErrorCode.FILE_ERROR.getMessage());
  }

  @Test
  void find_shouldReturn_whenExists() {
    UUID id = content.getId();
    given(binaryContentRepository.findById(id)).willReturn(Optional.of(content));

    BinaryContent result = binaryContentService.find(id.toString());

    assertThat(result).isEqualTo(content);
  }

  @Test
  void find_shouldThrow_whenNotExists() {
    UUID id = UUID.randomUUID();
    given(binaryContentRepository.findById(id)).willReturn(Optional.empty());

    assertThatThrownBy(() -> binaryContentService.find(id.toString()))
        .isInstanceOf(DiscodeitException.class)
        .hasMessageContaining(ErrorCode.IMAGE_NOT_FOUND.getMessage());
  }
}
