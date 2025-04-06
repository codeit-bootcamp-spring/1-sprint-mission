package com.sprint.mission.cascade;

import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.*;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.serviceImpl.BinaryServiceImpl;
import com.sprint.mission.service.jcf.serviceImpl.ReadStatusService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChannelCascadeTest {

    private static final Logger log = LoggerFactory.getLogger(ChannelCascadeTest.class);
    // Channel 삭제 시 : readStatus랑 Message 삭제되는지
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

    @BeforeEach
    void createPublicChannel() {
        for (int i = 0; i < 1; i++) {
            channelService.createPublicChannel(new PublicChannelCreateDTO("테스트 채널", "Spring 교육과정입니다."));
            userService.create(new UserDtoForCreate("testUser", "testPassword", "testEmail"), null);
        }
        em.flush();
        em.clear();
    }

    @DisplayName("채널 삭제 시 메시지와 읽음 상태도 삭제되는지")
    @Test
    void deleteChannel() {
        // Given1
        var deletingChannel = channelService.findAll().get(0);
        var channelId = deletingChannel.getId();
        UUID writerId = userService.findAll().get(0).getId();
        MessageDtoForCreate messageDtoForCreate = new MessageDtoForCreate(channelId, writerId, null);
        Message createdMessage = messageService.create(messageDtoForCreate, new ArrayList<>());
        ReadStatus readStatus = readStatusService.create(new ReadStatusCreateRequest(writerId, channelId, Instant.now()));

        em.flush();
        em.clear();

        // When1
        Message findedMessage = messageService.findById(createdMessage.getId());
        List<ReadStatus> readStatuses = readStatusService.findAllByChannelId(channelId);

        // Then1
        assertThat(findedMessage).isNotNull();
        assertThat(findedMessage.getChannel()).isEqualTo(deletingChannel);
        assertThat(readStatuses).hasSize(1);

        em.flush();
        em.clear();

        // Given2
        channelService.delete(channelId);

        // When2
        List<Message> allMessage = messageRepository.findAll();
        List<ReadStatus> readStatusList = readStatusRepository.findAll();


        // Then2
        log.info("Message가 삭제됐을까? {}", allMessage);  // []
        log.info("ReadStatus가 삭제됐을까? {}", readStatusList); // []
        assertThat(allMessage).hasSize(0);
        assertThat(readStatusList).hasSize(0);
    }


    @Autowired
    private BinaryServiceImpl binaryService;

    @Autowired
    private UserMapper userMapper;

    private User createUser(BinaryContentDtoForCreate dto, UserDtoForCreate userDto) {
        Optional<BinaryContentDtoForCreate> profileDto = Optional.of(dto);
        User createdUser = profileDto.map((binaryDto) -> {
            BinaryContent createdBinaryContent = binaryService.create(binaryDto);
            return userMapper.toEntityWithProfile(userDto, createdBinaryContent);
        }).orElseGet(() -> userMapper.toEntityWithoutProfile(userDto));
        return createdUser;
    }

    //public record MessageDtoForCreate(
    //        @NotNull(message = "채널 ID는 필수입니다.")
    //        UUID channelId,
    //        @NotNull(message = "유저 ID는 필수입니다.")
    //        UUID userId,
    //        @NotBlank(message = "내용은 필수입니다.")
    //        String content) {
    //}


}

//public record PublicChannelCreateDTO(
//
//        @Schema(example = "코드잇 채널")
//        @NotBlank(message = "이름은 필수입니다.")
//        @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
//        String name,
//
//        @Schema(example = "Spring 교육과정입니다.")
//        @NotBlank(message = "설명은 필수입니다.")
//        String description) {
