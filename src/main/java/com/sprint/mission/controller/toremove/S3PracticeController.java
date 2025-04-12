//package com.sprint.mission.controller.toremove;
//
//import com.sprint.mission.dto.response.BinaryContentDto;
//import com.sprint.mission.repository.S3BinaryContentStorage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//public class S3PracticeController {
//
//    private final S3BinaryContentStorage s3Storage;
//    private BinaryContentDto binaryContentDto;
//
//    @PostMapping("p-upload/s3")
//    public void uploadS3(@RequestParam("file") MultipartFile file) throws IOException {
//        binaryContentDto = createBinaryContentDto(file);
//        s3Storage.put(binaryContentDto.id(), binaryContentDto.bytes());
//    }
//
//    @GetMapping("p-download/s3")
//    public ResponseEntity<Void> downloadS3(){
//        return s3Storage.download(binaryContentDto);
//    }
//
//    private BinaryContentDto createBinaryContentDto(MultipartFile file) throws IOException {
//        UUID uuid = UUID.randomUUID();
//        String filename = uuid + ".jpg";
//        Long size = file.getSize();
//        String contentType = file.getContentType();
//        byte[] bytes = file.getBytes();
//        return new BinaryContentDto(uuid, filename, size, contentType, bytes);
//    }
//}
