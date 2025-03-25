package com.sprint.mission.BasicTest;

import com.sprint.mission.dto.MessageMapper;
import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.request.*;
import com.sprint.mission.dto.response.ScrollPageResponse;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.BinaryContentRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.main.JCFMessageService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JCFMessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private BinaryContentRepository binarycontentRepository;

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;


    @Test
    void createMessage(){
        // given
        UserDtoForCreate userDtoForCreate = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        User createdUser = userService.create(userDtoForCreate, null);

        PublicChannelCreateDTO publicChannelCreateDTO = new PublicChannelCreateDTO("testChannel", "testChannelName");
        Channel publicChannel = channelService.createPublicChannel(publicChannelCreateDTO);

        MessageDtoForCreate testMessageDto = new MessageDtoForCreate(publicChannel.getId(), createdUser.getId(), "testMessage");

        // when
        Message createdMessage = messageService.create(testMessageDto, new ArrayList<>());
//        System.out.println("======================EntityGraph 쿼리 시작======================");
//        List<Message> allByChannelId = messageService.findAllByChannelId(publicChannel.getId());
//        System.out.println("======================EntityGraph 쿼리 끝======================");
//
//        // then
//        assertThat(allByChannelId.size()).isEqualTo(1);
//        assertThat(createdMessage.getId()).isEqualTo(allByChannelId.get(0).getId());
        assertThat(createdMessage.getAuthor().getId()).isEqualTo(testMessageDto.userId());
        assertThat(createdMessage.getContent()).isEqualTo(testMessageDto.content());
    }

    @Test
    void updateMessage(){
        // given
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

        // when
        Message createdMessage = messageService.create(testMessageDto, binaryContentDtoList);
        System.out.println("업데이트 전 getContent() = " + createdMessage.getContent());
        createdMessage.update("새로운 컨텐츠");
        //Message updatedMessage = messageMapper.update(new MessageDtoForUpdate("새로운 컨텐츠"), createdMessage);
        System.out.println("업데이트 후 getContent() = " + createdMessage.getContent());

        MessageDto dto = messageMapper.toDto(createdMessage);

        assertThat(createdMessage.getContent()).isNotEqualTo(testMessageDto.content());
        assertThat(createdMessage.getId()).isEqualTo(createdMessage.getId());
        assertThat(createdMessage.getAuthor().getId()).isEqualTo(testMessageDto.userId());
        assertThat(dto.attachments().size()).isEqualTo(3);
        System.out.println("dto.attachments() = " + dto.attachments());
        System.out.println("dto.author() = " + dto.author());

    }

    @Test
    @Transactional
    void setting(){
        UserDtoForCreate userDtoForCreate = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        User createdUser = userService.create(userDtoForCreate, null);

        PublicChannelCreateDTO publicChannelCreateDTO = new PublicChannelCreateDTO("testChannel", "testChannelName");
        Channel publicChannel = channelService.createPublicChannel(publicChannelCreateDTO);

        List<Message> messageList = new ArrayList<>();
        Pageable pageable = Pageable.ofSize(50);
        for (int i = 0; i < 122; i++) {
            String fileNumber = i + "테스트 용 bytes";
            messageList.add(new Message(publicChannel, createdUser, "testMessage" + i));
        }
        messageRepository.saveAll(messageList);
        // 중간 점검
        List<UserStatus> userStatusList = userStatusRepository.findAll();
        System.out.println("userStatusList = " + userStatusList);
        assertThat(messageList.size()).isEqualTo(122);
        assertThat(userStatusList.get(0).getUser()).isEqualTo(createdUser);
        em.flush();
        em.clear();

//        List<Message> savedMessageInChannel = messageRepository.findAllByChannel_Id(publicChannel.getId());
//        assertThat(savedMessageInChannel.size()).isEqualTo(102);
        System.out.println("messageList.get(0).getAuthor().getStatus() = " + messageList.get(0).getAuthor().getStatus());



        //List<PageResponse<MessageDto>> pageResponseList = messageService.findAllByChannelId(publicChannel.getId(), pageable);
        List<ScrollPageResponse<MessageDto>> scrollPageList = messageService.findAllByChannelId(publicChannel.getId());
        assertThat(scrollPageList.size()).isEqualTo(3);

    }
}
