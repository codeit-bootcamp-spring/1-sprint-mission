package com.sprint.mission.discodeit.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.async.event.FileUploadEvent;
import com.sprint.mission.discodeit.async.handler.FileUploadEventHandler;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ActiveProfiles("test")
class FileUploadEventHandlerTest {

  @Autowired
  BinaryContentStorage binaryContentStorage; // 실제 구현체

  @Autowired
  FileUploadEventHandler uploadEventHandler;

  MultipartFile testImageFile;
  UUID testFileId;
  Path rootDir;

  @BeforeEach
  void setup() throws IOException {
    ClassPathResource image = new ClassPathResource("static/test-image.png");
    testImageFile = new MockMultipartFile("file", image.getFilename(), "image/png",
        image.getInputStream());
    testFileId = UUID.randomUUID();
    rootDir = Paths.get(System.getProperty("user.dir")).resolve("temp");
  }

  @Test
  @DisplayName("파일 업로드 이벤트 수신 시 binaryContentStorage.put을 호출한다.")
  void uploadFileCalledWithCorrectParams() {
    // given
    FileUploadEvent event = new FileUploadEvent(testFileId, testImageFile);

    // when
    uploadEventHandler.handle(event);

    // then
    verify(binaryContentStorage, times(1)).put(eq(testFileId), eq(testImageFile));
  }

  @Test
  @DisplayName("비동기 파일 저장 로직을 통해 성공적으로 파일을 저장한다.")
  void saveLogicCanSaveFileSuccess() throws IOException {
    // given
    FileUploadEvent event = new FileUploadEvent(testFileId, testImageFile);

    // when
    uploadEventHandler.handle(event);

    // then
    String filename = testImageFile.getOriginalFilename();
    String extension = filename.substring(filename.lastIndexOf('.'));
    Path expectedPath = rootDir.resolve(testFileId.toString() + extension);

    await()
        .atMost(2, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(Files.exists(expectedPath)).isTrue());

    Files.deleteIfExists(expectedPath);
  }


}