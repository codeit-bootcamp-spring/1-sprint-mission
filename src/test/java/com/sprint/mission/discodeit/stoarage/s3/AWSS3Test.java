package com.sprint.mission.discodeit.stoarage.s3;

import com.sprint.mission.discodeit.storage.s3.S3Service;
import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)  // 같은 클래스끼리 같은 테스트 인스턴스 사용
public class AWSS3Test {

  private S3Service s3Service;

  @BeforeAll
  void setup() {
    s3Service = new S3Service();
  }

  @Test
  void testUpload() {
    // given
    String key = "test-folder/sample.txt";
    String filePath = "src/test/resources/sample.txt";

    // when
    s3Service.upload(key, filePath);

    // then
    System.out.println("Upload Success");
  }

  @Test
  void testDownload() {
    // given
    String key = "test-folder/sample.txt";
    String downloadPath = "src/test/resources/downloaded.txt";

    // when
    s3Service.download(key, downloadPath);

    // then
    File file = new File(downloadPath);
    Assertions.assertTrue(file.exists());
  }

  @Test
  void testGeneratePresignedUrl() {
    // given
    String key = "test-folder/sample.txt";

    // when
    URL url = s3Service.generatePresignedUrl(key);

    // then
    System.out.println("Presigned URL: " + url.toString());
    Assertions.assertNotNull(url);
  }
}
