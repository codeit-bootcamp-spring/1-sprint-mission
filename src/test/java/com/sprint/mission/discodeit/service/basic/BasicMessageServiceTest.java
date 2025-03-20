package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicMessageServiceTest {

  @Autowired
  private MessageService messageService;
  @Autowired
  private UserService userService;
  @Autowired
  private ChannelService channelService;
  @Autowired
  private PlatformTransactionManager transactionManager;

  MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.txt",
      "text/plain",
      "dadwqdczxcqwddqdwd".getBytes()
  );
  PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
      "public channel1", "공개 채널");
  ChannelDto channelDto = null;


  @Test
  @DisplayName("메세지 생성")
  void create() {
    UUID authorId = userService.findAll().get(0).id();
    MessageCreateRequest request = new MessageCreateRequest("hi hello", channelDto.id(), authorId);
    MessageDto messageDto = messageService.create(request, List.of(file));

    assertThat(messageDto.content()).isEqualTo(request.content());
    assertThat(messageDto.author().id()).isEqualTo(authorId);
    assertThat(messageDto.channelId()).isEqualTo(channelDto.id());
  }

  @Test
  @DisplayName("채널의 메세지 목록 조회")
  void messages() {
    for (int i = 0; i < 100; i++) {
      createMessage();
    }

    PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Direction.DESC, "createdAt"));

    PageResponse<Message> pagedMessages = messageService.findMessagesWithPaging(
        channelDto.id(), pageRequest);

    assertThat(pagedMessages.getTotalElements()).isEqualTo(100);
    assertThat(pagedMessages.isHasNext()).isTrue();
    assertThat(pagedMessages.getSize()).isEqualTo(50);
  }

  @Test
  @DisplayName("메세지 삭제")
  void delete() {
    MessageDto messageDto = createMessage();

    messageService.remove(messageDto.id());

    PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Direction.DESC, "createdAt"));
    PageResponse<Message> pagedMessages = messageService.findMessagesWithPaging(
        channelDto.id(), pageRequest);

    assertThat(pagedMessages.getContent().isEmpty()).isTrue();
  }

  @Test
  @DisplayName("메세지 내용 수정")
  void update() {
    createMessage();
    PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Direction.DESC, "createdAt"));
    PageResponse<Message> pagedMessages = messageService.findMessagesWithPaging(
        channelDto.id(), pageRequest);

    Message message = pagedMessages.getContent().get(0);

    MessageDto newContent = messageService.update(message.getId(),
        new MessageContentUpdateRequest("new Content"));

    assertThat(newContent.content()).isEqualTo("new Content");
  }


  private MessageDto createMessage() {
    UUID authorId = userService.findAll().get(0).id();
    MessageCreateRequest request = new MessageCreateRequest("hi hello", channelDto.id(), authorId);
    MessageDto messageDto = messageService.create(request, List.of(file));
    return messageDto;
  }

  @BeforeAll
  void setupTestData() {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    transactionTemplate.execute(status -> {
      for (int i = 1; i <= 2; i++) {
        String username = "user" + i;
        String email = "user" + i + "@mail.com";

        UserCreateRequest request = UserCreateRequest.builder()
            .username(username)
            .email(email)
            .password("password123")
            .build();

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "profile" + i + ".txt",
            "text/plain",
            ("dummy content for user " + i).getBytes()
        );

        try {
          userService.join(request, file);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      channelDto = channelService.createPublic(publicChannelCreateRequest);

      return null;
    });
  }
}
