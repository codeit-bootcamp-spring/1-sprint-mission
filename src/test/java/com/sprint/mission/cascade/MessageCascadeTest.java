package com.sprint.mission.cascade;

import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.BinaryContentRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.addOn.ReadStatusService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MessageCascadeTest {

    private static final Logger log = LoggerFactory.getLogger(MessageCascadeTest.class);
    @Autowired
    private ChannelService channelService;

    @Autowired
    private ReadStatusService readStatusService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EntityManager em;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;
    @Autowired
    private BinaryContentRepository binaryContentRepository;

    // 메시지의 첨부파일이 사라지면 메시지는 사라지지 않고 메시지의 첨부파일이 NULL값으로 변경된다
    @BeforeEach
    void setting() {
        // Given
        UserDtoForCreate userDtoForCreate = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        User createdUser = userService.create(userDtoForCreate, null);

        PublicChannelCreateDTO publicChannelCreateDTO = new PublicChannelCreateDTO("testChannel", "testChannelName");
        Channel publicChannel = channelService.createPublicChannel(publicChannelCreateDTO);

        MessageDtoForCreate testMessageDto = new MessageDtoForCreate(publicChannel.getId(), createdUser.getId(), "testMessage");
        List<BinaryContentDtoForCreate> binaryContentDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String fileNumber = i + "테스트 용 bytes";
            binaryContentDtoList.add(new BinaryContentDtoForCreate("testFile" + i, "image/png", 123L, fileNumber.getBytes()));
        }
        messageService.create(testMessageDto, binaryContentDtoList);
        em.flush();
        em.clear();
    }

    @Test
    void cascade(){
        List<Message> messageList = messageRepository.findAll();
        System.out.println("messageList = " + messageList);
        assertThat(messageList).hasSize(1);

        Message message = messageList.get(0);
        UUID messageID = message.getId();


        List<BinaryContent> binaryContentList = binaryContentRepository.findAll();
        assertThat(binaryContentList).hasSize(3);

        //UUID binaryId = attachments.get(0).getId();
        System.out.println("메시지 삭제");
        messageService.delete(messageID);
        em.flush();
        em.clear();

        List<BinaryContent> emptyBinary = binaryContentRepository.findAll();

        assertThat(emptyBinary).hasSize(0);
        // OneToMany 관계를 JoinTable로 설계도가 되어있어서....
        //Message message = messageRepository.findAll().get(0);
    }
}
