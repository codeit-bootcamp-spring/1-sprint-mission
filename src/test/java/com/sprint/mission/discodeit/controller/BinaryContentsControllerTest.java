package com.sprint.mission.discodeit.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.TestConfig;
import com.sprint.mission.discodeit.entity.binarycontent.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@Import(TestConfig.class)
@WebMvcTest(BinaryContentsController.class)
@SuppressWarnings("unchecked")
class BinaryContentsControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  BinaryContentService binaryContentService;

  @Test
  @DisplayName("GET /api/binaryContents/{binaryCotentId}/download - 파일 다운로드")
  void download() throws Exception {
    UUID binaryId = UUID.randomUUID();

    byte[] content = "테스트".getBytes();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    httpHeaders.setContentDisposition(ContentDisposition.attachment()
        .filename("test.txt")
        .build());

    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(content, httpHeaders,
        HttpStatus.OK);

    when(binaryContentService.downloadBinaryContent(any(UUID.class)))
        .thenReturn((ResponseEntity) responseEntity);

    mockMvc
        .perform(get("/api/binaryContents/{binaryContentId}/download", binaryId))
        .andExpect(status().isOk())
        .andExpect(content().bytes(content));

    verify(binaryContentService).downloadBinaryContent(binaryId);
  }

  @Test
  @DisplayName("GET /api/binaryContents/{binaryCotentId} - 파일 단건 조회")
  void findFile() throws Exception {
    UUID binaryId = UUID.randomUUID();

    BinaryContentResponse response = new BinaryContentResponse(UUID.randomUUID(),
        "test.txt", 8192L, "plain/txt");
    when(binaryContentService.find(binaryId))
        .thenReturn(response);

    mockMvc
        .perform(get("/api/binaryContents/{binaryContentId}", binaryId))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(binaryContentService).find(binaryId);
  }
}