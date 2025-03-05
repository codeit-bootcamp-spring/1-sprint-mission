package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.binarycontent.UploadBinaryContent;
import com.sprint.mission.discodeit.exception.binary.BinaryContentException;
import com.sprint.mission.discodeit.exception.binary.BinaryContentNotFoundException;
import java.time.Instant;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class BinaryContentServiceManager {

  @Value("${file.dir}")
  private String fileDir;

  public UploadBinaryContent saveFile(BinaryContentRequest request) throws IOException {
    if (request.getFile() == null) {
      throw new BinaryContentNotFoundException();
    }

    // 파일 이름 가져오기
    String originalFilename = request.getFile().getOriginalFilename();
    if (originalFilename.isEmpty()) {
      throw new BinaryContentException("파일 이름이 잘못되었습니다.");
    }

    try {
      byte[] bytes = request.getFile().getBytes();

      // 파일 이름을 uuid로 변환
      String savedFileName = getUploadFileName(originalFilename);

      // 저장 디렉토리 확인 및 생성
      File directory = new File(fileDir);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      // 업로드
      File destFile = new File(getFullPath(savedFileName));
      request.getFile().transferTo(destFile);

      log.info("파일 저장 완료, 파일 이름: {}", savedFileName);

      return new UploadBinaryContent(request.getRequestUserId(), originalFilename, savedFileName,
          Instant.now(), request.getFile().getSize(), request.getFile().getContentType(),
          Base64.getEncoder().encodeToString(bytes));

    } catch (IOException e) {
      log.error("파일 업로드 실패: {}", originalFilename, e);
      throw new BinaryContentException("파일 업로드 실패: " + e.getMessage());
    }
  }

  public List<UploadBinaryContent> saveFiles(BinaryContentRequest request) throws IOException {
    if (request.getFiles() == null) {
      return new ArrayList<>();
    }

    List<UploadBinaryContent> uploadList = new ArrayList<>();

    for (MultipartFile multipartFile : request.getFiles()) {
      if (multipartFile != null && !multipartFile.isEmpty()) {
        try {
          BinaryContentRequest binaryContentRequest = new BinaryContentRequest(
              multipartFile.getOriginalFilename(), multipartFile, null);
          binaryContentRequest.updateId(request.getRequestUserId());

          UploadBinaryContent uploadBinaryContent = saveFile(binaryContentRequest);
          uploadList.add(uploadBinaryContent);
        } catch (IOException e) {
          log.error("파일 저장 실패: ", e);
        }
      }
    }

    return uploadList;
  }

  private static String getUploadFileName(String originalFilename) {
    String uuid = UUID.randomUUID().toString();
    String ext = getExt(originalFilename);
    return uuid + "." + ext;
  }

  private static String getExt(String originalFilename) {
    int idx = originalFilename.lastIndexOf(".");
    if (idx == -1) {
      return ""; // 확장자가 없는 경우
    }
    return originalFilename.substring(idx + 1);
  }

  public String getFullPath(String fileName) {
    return Paths.get(fileDir, fileName).toString();
  }

  public UUID formatToUUID(String uuidString) {
    // 입력된 문자열에서 하이픈을 모두 제거하고 하이픈이 포함된 표준 UUID 형식으로 변환
    String formattedUUID = uuidString.replaceAll(
        "(.{8})(.{4})(.{4})(.{4})(.+)",
        "$1-$2-$3-$4-$5"
    );

    // 포맷된 문자열을 UUID 객체로 변환
    return UUID.fromString(formattedUUID);
  }
}