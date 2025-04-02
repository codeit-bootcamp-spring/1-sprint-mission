package com.sprint.mission.discodeit.cotroller;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;

import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MessageService messageService;

  private UUID channelId;
  private List<MessageDto> messageDtos;
  private PageResponse<MessageDto> pageResponse;
  private UserDto authorDto;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();

    authorDto = UserDto.builder()
        .id(UUID.randomUUID())
        .username("testUser")
        .email("test@example.com")
        .online(true)
        .build();

    messageDtos = List.of(
        MessageDto.builder()
            .id(UUID.randomUUID())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .content("Test Message 1")
            .channelId(channelId)
            .author(authorDto)
            .build(),
        MessageDto.builder()
            .id(UUID.randomUUID())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .content("Test Message 2")
            .channelId(channelId)
            .author(authorDto)
            .build()
    );

    Page<MessageDto> page = new PageImpl<>(
        messageDtos,
        PageRequest.of(0, 2, Sort.by("createdAt").ascending()),
        messageDtos.size()
    );

    pageResponse = new PageResponse<>(messageDtos,
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements());

  }

  @Test
  @DisplayName("GET /api/messages - 성공")
  void getMessageByChannelId_Success() throws Exception {
    given(messageService.findAllByChannelId(
        any(UUID.class),
        any(Pageable.class))
    ).willReturn(pageResponse);

    mockMvc.perform(
            get("/api/messages")
                .param("channelId", channelId.toString())
                .param("page", "0")
                .param("size", "2")
                .param("sortBy", "createdAt")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].content").value("Test Message 1"))
        .andExpect(jsonPath("$.content[1].content").value("Test Message 2"))
        .andExpect(jsonPath("$.content[0].author.username").value("testUser"));
  }
  
  // TODO 실패

}
