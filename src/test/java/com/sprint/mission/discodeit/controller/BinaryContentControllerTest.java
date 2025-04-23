package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BinaryContentController.class)
class BinaryContentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private BinaryContentService binaryContentService;

  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  @Test
  @DisplayName("바이너리 컨텐츠 조회 성공 테스트")
  void find_Success() throws Exception {
    // Given
    UUID binaryContentId = UUID.randomUUID();
    BinaryContentDto binaryContent = new BinaryContentDto(
        binaryContentId,
        "test.jpg",
        10240L,
        MediaType.IMAGE_JPEG_VALUE
    );

    given(binaryContentService.find(binaryContentId)).willReturn(binaryContent);

    // When & Then
    mockMvc.perform(get("/api/binaryContents/{binaryContentId}", binaryContentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(binaryContentId.toString()))
        .andExpect(jsonPath("$.fileName").value("test.jpg"))
        .andExpect(jsonPath("$.size").value(10240))
        .andExpect(jsonPath("$.contentType").value(MediaType.IMAGE_JPEG_VALUE));
  }

  @Test
  @DisplayName("바이너리 컨텐츠 조회 실패 테스트 - 존재하지 않는 컨텐츠")
  void find_Failure_BinaryContentNotFound() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    given(binaryContentService.find(nonExistentId))
        .willThrow(BinaryContentNotFoundException.withId(nonExistentId));

    // When & Then
    mockMvc.perform(get("/api/binaryContents/{binaryContentId}", nonExistentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("ID 목록으로 바이너리 컨텐츠 조회 성공 테스트")
  void findAllByIdIn_Success() throws Exception {
    // Given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    List<UUID> binaryContentIds = List.of(id1, id2);

    List<BinaryContentDto> binaryContents = List.of(
        new BinaryContentDto(id1, "test1.jpg", 10240L, MediaType.IMAGE_JPEG_VALUE),
        new BinaryContentDto(id2, "test2.pdf", 20480L, MediaType.APPLICATION_PDF_VALUE)
    );

    given(binaryContentService.findAllByIdIn(binaryContentIds)).willReturn(binaryContents);

    // When & Then
    mockMvc.perform(get("/api/binaryContents")
            .param("binaryContentIds", id1.toString(), id2.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(id1.toString()))
        .andExpect(jsonPath("$[0].fileName").value("test1.jpg"))
        .andExpect(jsonPath("$[1].id").value(id2.toString()))
        .andExpect(jsonPath("$[1].fileName").value("test2.pdf"));
  }

  @Test
  @DisplayName("바이너리 컨텐츠 다운로드 성공 테스트")
  void download_Success() throws Exception {
    // Given
    UUID binaryContentId = UUID.randomUUID();
    BinaryContentDto binaryContent = new BinaryContentDto(
        binaryContentId,
        "test.jpg",
        10240L,
        MediaType.IMAGE_JPEG_VALUE
    );

    given(binaryContentService.find(binaryContentId)).willReturn(binaryContent);

    // doReturn 사용하여 타입 문제 우회
    ResponseEntity<ByteArrayResource> mockResponse = ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.jpg\"")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
        .body(new ByteArrayResource("test data".getBytes()));

    doReturn(mockResponse).when(binaryContentStorage).download(any(BinaryContentDto.class));

    // When & Then
    mockMvc.perform(get("/api/binaryContents/{binaryContentId}/download", binaryContentId))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("바이너리 컨텐츠 다운로드 실패 테스트 - 존재하지 않는 컨텐츠")
  void download_Failure_BinaryContentNotFound() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    given(binaryContentService.find(nonExistentId))
        .willThrow(BinaryContentNotFoundException.withId(nonExistentId));

    // When & Then
    mockMvc.perform(get("/api/binaryContents/{binaryContentId}/download", nonExistentId))
        .andExpect(status().isNotFound());
  }
} 