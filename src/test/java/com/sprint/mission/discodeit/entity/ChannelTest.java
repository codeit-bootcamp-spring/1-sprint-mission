package com.sprint.mission.discodeit.entity;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



@DisplayName("Channel Test")
//@ExtendWith(MockitoExtension.class) // junit5 와 mockito 쓰기 위해 선언해야함!
class ChannelTest {

    private static final Logger log = LoggerFactory.getLogger(ChannelTest.class);
    Channel channel = new Channel("테스트 체널 입니다.");

    @BeforeEach
    void testStart() {
        log.info("======================== testStart ===========================");
    }

    @AfterEach
    void testEnd() {
        log.info("======================== Test End ============================");
    }

    @Test
    @DisplayName("채널 이름 테스트")
    void testChannelName() {
        assertEquals("테스트 체널 입니다.", channel.getChannelName(), "채널 이름이 일치해야 합니다.");
    }

    @Test
    @DisplayName("채널 이름 업데이트 테스트")
    void testUpdateChannelName() {
        channel.update("업데이트된 채널 이름");
        assertEquals("업데이트된 채널 이름", channel.getChannelName(), "채널 이름이 업데이트되어야 합니다.");
    }

}