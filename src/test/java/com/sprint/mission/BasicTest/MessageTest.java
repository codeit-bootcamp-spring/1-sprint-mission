package com.sprint.mission.BasicTest;

import com.sprint.mission.dto.MessageMapper;
import com.sprint.mission.dto.mappedDto.MessageDto;
import com.sprint.mission.dto.request.*;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.BinarycontentRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageTest {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private BinarycontentRepository binarycontentRepository;


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
        System.out.println("======================EntityGraph 쿼리 시작======================");
        List<Message> allByChannelId = messageService.findAllByChannelId(publicChannel.getId());
        System.out.println("======================EntityGraph 쿼리 끝======================");

        // then
        assertThat(allByChannelId.size()).isEqualTo(1);
        assertThat(createdMessage.getId()).isEqualTo(allByChannelId.get(0).getId());
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
        Message updatedMessage = messageMapper.update(new MessageDtoForUpdate("새로운 컨텐츠"), createdMessage);
        System.out.println("업데이트 후 getContent() = " + updatedMessage.getContent());

        MessageDto dto = messageMapper.toDto(updatedMessage);

        List<BinaryContent> all = binarycontentRepository.findAll();
        System.out.println("가자~~~~~~~~~~~~~~~~~");
        all.forEach(System.out::println);

        assertThat(updatedMessage.getContent()).isNotEqualTo(testMessageDto.content());
        assertThat(updatedMessage.getId()).isEqualTo(createdMessage.getId());
        assertThat(updatedMessage.getAuthor().getId()).isEqualTo(testMessageDto.userId());
        assertThat(dto.attachments().size()).isEqualTo(3);
        System.out.println("dto.attachments() = " + dto.attachments());
        System.out.println("dto.author() = " + dto.author());

    }

    void setting(){

    }
}
