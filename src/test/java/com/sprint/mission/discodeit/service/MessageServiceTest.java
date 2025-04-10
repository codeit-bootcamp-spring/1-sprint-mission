package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private PageResponseMapper pageResponseMapper;
  @Mock
  private MessageMapper messageMapper;
  @InjectMocks
  private MessageService messageService;

  @Nested
  @DisplayName("메세지 생성 테스트")
  class createMessage {

    @Test
    @DisplayName("생성 성공")
    void createMessageSuccess() {
      
    }

    @Test
    @DisplayName("생성 실패")
    void createMessageFailure() {

    }
  }

  @Nested
  @DisplayName("메세지 수정 테스트")
  class updateMessage {

    @Test
    @DisplayName("메세지 수정 성공")
    void updateMessageSuccess() {

    }

    @Test
    @DisplayName("메세지 수정 실패 - 존재하지 않는 메세지")
    void updateMessageNotFoundFailure() {

    }

    @Test
    @DisplayName("메세지 수정 실패 - 메세지 작성자가 아님")
    void updateMessageByOtherFailure() {

    }

  }

  @Nested
  @DisplayName("메세지 삭제 테스트")
  class deleteMessage {

    @Test
    @DisplayName("메세지 삭제 성공")
    void deleteMessageSuccess() {

    }

    @Test
    @DisplayName("메세지 삭제 실패")
    void deleteMessageFailure() {

    }
  }

  @Nested
  @DisplayName("메세지 조회")
  class getMessage {

    @Test
    @DisplayName("Channel Id로 조회 성공")
    void getMessagesByChannelIdSuccess() {
    }


    @Test
    @DisplayName("Channel Id로 조회 실패")
    void getMessagesByChannelIdFailure() {
    }
  }
}
