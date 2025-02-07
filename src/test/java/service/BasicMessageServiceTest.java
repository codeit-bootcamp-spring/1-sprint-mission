package service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private EntityValidator validator;

  @InjectMocks
  private BasicMessageService messageService;

  private Message message1;
  private Message message2;
  private Message message3;

  @BeforeEach
  void setUp(){

    message1 = new Message.MessageBuilder("user1","channel1", "content1").build();
    message2 = new Message.MessageBuilder("user2", "channel2", "content2").build();
    message3 = new Message.MessageBuilder("user1", "channel1", "content2").binaryContentId("binary1").build();

  }


}
