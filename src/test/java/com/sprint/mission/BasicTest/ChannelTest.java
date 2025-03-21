package com.sprint.mission.BasicTest;

import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.service.ChannelService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.entity.main.ChannelType.PRIVATE;
import static com.sprint.mission.entity.main.ChannelType.PUBLIC;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ChannelTest {

    private static final Logger log = LoggerFactory.getLogger(ChannelTest.class);
    @Autowired
    private ChannelService channelService;


    @DisplayName("PublicChannel 생성")
    @Test
    void createPublicService() {
        PublicChannelCreateDTO publicChannelCreateDTO = new PublicChannelCreateDTO("testChannel 1", "testChannelName 1");
        Channel publicChannel = channelService.createPublicChannel(publicChannelCreateDTO);
        assertThat(publicChannel).isNotNull();

        Channel findedChannel = channelService.findById(publicChannel.getId());
        log.info("id : {}", findedChannel.getId());
        assertThat(findedChannel).isNotNull();
        assertThat(findedChannel.getName()).isEqualTo(publicChannelCreateDTO.name());
        assertThat(findedChannel.getDescription()).isEqualTo(publicChannelCreateDTO.description());
        assertThat(findedChannel.getChannelType()).isEqualTo(PUBLIC);
    }

    @DisplayName("PrivateChannel 생성")
    @Test
    void createPrivateService(){
        PrivateChannelCreateDTO privateChannelCreateDTO = new PrivateChannelCreateDTO(new ArrayList<>());
        Channel privateChannel = channelService.createPrivateChannel(privateChannelCreateDTO);
        assertThat(privateChannel).isNotNull();

        Channel findedChannel = channelService.findById(privateChannel.getId());
        assertThat(findedChannel).isNotNull();
        // name 이랑 description은 null 이여야 됨
        assertThat(findedChannel.getName()).isNull();
        assertThat(findedChannel.getDescription()).isNull();
        assertThat(findedChannel.getChannelType()).isEqualTo(PRIVATE);
    }
}
