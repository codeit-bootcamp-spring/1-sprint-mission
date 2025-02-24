package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.enums.ContentType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileConverter {
    public static BinaryContentDTO.request convertToBinaryContent(MultipartFile file, UUID referenceId, ContentType contentType) {
        try {
            return BinaryContentDTO.request.builder()
                    .file(file.getBytes())  // MultipartFile → byte[]
                    .referenceId(referenceId)
                    .mimeType(file.getContentType()) // MIME 타입 가져오기
                    .filename(file.getOriginalFilename()) // 파일명 가져오기
                    .contentType(contentType)
                    .build();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    public static byte[] convertToFile(byte[] fileData, String filename) throws IOException {

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, getFileExtension(filename), outputStream);
            return outputStream.toByteArray();
        }
    }

    public static byte[] zipFiles(List<BinaryContentDTO.convert> contentList) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (BinaryContentDTO.convert content : contentList) {
                ZipEntry zipEntry = new ZipEntry(content.filename());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(content.file());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();   // 명시적으로 닫아줘야함!

            return byteArrayOutputStream.toByteArray();
        }
    }

    // 파일 확장자 추출 메서드
    private static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ""; // 확장자가 없을 경우 빈 문자열 반환
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
