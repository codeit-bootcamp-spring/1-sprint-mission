package com.sprint.mission.mapper;

import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.UserService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MapperTest {

    private static final Logger log = LoggerFactory.getLogger(MapperTest.class);
    @Autowired
    private UserService userService;
    @Autowired
    private BinaryContentMapper binaryContentMapper;

    @Autowired
    private EntityManager em;
    @Autowired
    private ChannelService channelService;

    @Test
    @Transactional
    void publicChannelTypeTest() {
        PublicChannelCreateDTO createDTO = new PublicChannelCreateDTO("코드잇 채널", "Spring 교육과정입니다.");
        log.info("createDTO = {}", createDTO);
        Channel createdPublicChannel = channelService.createPublicChannel(createDTO);
        em.flush();
        em.clear();

        Channel findedChannel = channelService.findById(createdPublicChannel.getId());
        assertThat(findedChannel).isEqualTo(createdPublicChannel);
        assertThat(findedChannel.getChannelType()).isEqualTo(ChannelType.PUBLIC);
        log.info("findedChannel = {}", findedChannel);
    }


    @Test
    @Transactional
    void privateChannelTypeTest() {
        PrivateChannelCreateDTO createDTO = new PrivateChannelCreateDTO(new ArrayList<>());
        log.info("createDTO = {}", createDTO);
        Channel createdPublicChannel = channelService.createPrivateChannel(createDTO);
        em.flush();
        em.clear();

        Channel findedChannel = channelService.findById(createdPublicChannel.getId());
        assertThat(findedChannel).isEqualTo(createdPublicChannel);
        assertThat(findedChannel.getChannelType()).isEqualTo(ChannelType.PRIVATE);
        log.info("findedChannel = {}", findedChannel);
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
    //}

    //public record PrivateChannelCreateDTO(
    ////        @NotEmpty(message = "참여자 ID는 필수입니다.")
    //        List<UUID> participantIds) {
    //}
}
