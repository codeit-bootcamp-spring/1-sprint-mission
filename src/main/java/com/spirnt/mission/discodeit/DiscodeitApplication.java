package com.spirnt.mission.discodeit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscodeitApplication {

  static void clearDataFiles(String directory) {
    // 현재 작업 디렉토리에 있는 file-data-map 경로
    Path basePath = Paths.get(System.getProperty("user.dir"), directory);

    // 삭제할 폴더 목록
    List<String> folders = List.of("User", "Channel", "Message", "ReadStatus", "UserStatus",
        "BinaryContent");

    for (String folder : folders) {
      Path folderPath = basePath.resolve(folder);
      if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
        try (Stream<Path> files = Files.list(folderPath)) {
          files
              .filter(path -> path.toString().endsWith(".ser")) // .ser 파일만 선택
              .forEach(path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  System.out.println("Error:" + "파일 삭제 실패: " + path + " - " + e.getMessage());
                }
              });
        } catch (IOException e) {
          System.out.println("Error:" + "폴더 접근 실패: " + folderPath + " - " + e.getMessage());
        }
      }
    }

//    File folder = new File(System.getProperty("user.dir"), "uploadedFiles");
//
//    // 폴더가 존재하는지 확인
//    if (folder.exists() && folder.isDirectory()) {
//      File[] files = folder.listFiles(); // 폴더 내 파일 목록 가져오기
//
//      if (files != null) {
//        for (File file : files) {
//          if (file.isFile()) {
//            file.delete();
//          }
//        }
//      }
//    } else {
//      System.out.println("uploadedFiles 폴더가 존재하지 않습니다.");
//    }
  }


  public static void main(String[] args) {

    //clearDataFiles("file-data-map");

    SpringApplication.run(DiscodeitApplication.class, args);
  }

}
