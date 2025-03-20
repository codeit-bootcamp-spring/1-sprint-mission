package com.sprint.mission.discodeit.service.binarycontent;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.binarycontent.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Rollback(false)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinaryContentServiceImplTest {

  @Autowired
  private BinaryContentService binaryContentService;
  @Autowired
  private UserService userService;

  @Test
  @DisplayName("첨부 파일 조회")
  void findFile() {
    BinaryContentDto profile = userService.findAll().get(0).profile();

    BinaryContentResponse response = binaryContentService.find(profile.id());

    assertThat(response.fileName()).isEqualTo(profile.fileName());
    assertThat(response.contentType()).isEqualTo(profile.contentType());
    assertThat(response.size()).isEqualTo(profile.size());
  }

  @Test
  @DisplayName("여러 첨부파일 조회")
  void findFiles() {
    UUID profile1 = userService.findAll().get(0).profile().id();
    UUID profile2 = userService.findAll().get(1).profile().id();
    List<String> binaryContentIds = Arrays.asList(profile1.toString(), profile2.toString());
    System.out.println("============");

    List<BinaryContentResponse> files = binaryContentService.findAll(binaryContentIds);

    assertThat(files.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("파일 다운로드")
  void download() {
    UUID profile1 = userService.findAll().get(0).profile().id();
    System.out.println("==============");

    binaryContentService.downloadBinaryContent(profile1);
  }

  @BeforeAll
  void setupTestData() throws IOException {
    for (int i = 1; i <= 2; i++) {
      String username = "user" + i;
      String email = "user" + i + "@mail.com";

      UserCreateRequest request = UserCreateRequest.builder()
          .username(username)
          .email(email)
          .password("password123")
          .build();

      MockMultipartFile file = new MockMultipartFile(
          "file",
          "profile" + i + ".txt",
          "text/plain",
          ("dummy content for user " + i).getBytes()
      );

      userService.join(request, file);
    }
  }
}