package com.sprint.mission.unit.util;

import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MockFileFactory {

  public List<MockMultipartFile> getMockFileList(int number) {
    List<MockMultipartFile> mockMultipartFiles = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      mockMultipartFiles.add(
          new MockMultipartFile("파일 " + i, i + "번째 MockFile" + ".png", "image/png",
              "mockFile".getBytes()));
    }
    return mockMultipartFiles;
  }
}
