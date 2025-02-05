package com.sprint.mission.discodeit.util;

public class FileTypeProcessor {
  public static FileType process(String fileType) {
    return switch (fileType) {
      case "jpg" -> FileType.JPG;
      case "gif" -> FileType.GIF;
      case "mp4" -> FileType.MP4;
      default -> throw new IllegalArgumentException("지원되지 않는 파일 형식입니다");
    };
  }
}
