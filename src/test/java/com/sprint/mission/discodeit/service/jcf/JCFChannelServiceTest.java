package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JCFChannelServiceTest {
    private JCFChannelService channelService;

    @BeforeEach
    void setUp() {
        channelService = new JCFChannelService();
    }

    @DisplayName("채널 생성 테스트")
    @Test
    void testCreateChannel() {
        // when
        channelService.createChannel("channel1", null);
        channelService.createChannel("channel2", null);

        // then
        assertEquals(2, channelService.findAllChannels().size());
        assertEquals("channel1", channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getName());
    }

    @DisplayName("채널 이름 업데이트 테스트")
    @Test
    void testUpdateChannelName() {
        // given
        channelService.createChannel("channel3", null);

        // when
        String newName = "newChannel3";
        channelService.updateChannelName(channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getId(), newName);

        // then
        assertEquals(newName, channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getName());
    }

    @DisplayName("채널 삭제 테스트")
    @Test
    void testRemoveChannel() {
        // given
        channelService.createChannel("channel4", null);

        // when
        channelService.removeChannel(channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getId());

        // then
        assertEquals(0, channelService.findAllChannels().size());
    }

    @DisplayName("채널 멤버 업데이트 테스트")
    @Test
    void testUpdateMember() {
        // given
        channelService.createChannel("channel5", null);

        // when
        channelService.updateMember(channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getId(), null);

        // then
        assertNull(channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getMembers());
    }

    @DisplayName("메시지 전송 테스트")
    @Test
    void testSendMessage() {
        // given
        channelService.createChannel("channel6", null);
        User sender = new User("1111", "Alice");
        Message message = Message.ofCommon(sender, "HI");

        // when
        channelService.sendMessage(channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getId(), message);

        // then
        assertEquals(1, channelService.findChannelById(channelService.findAllChannels().get(0).getId()).getMessages().size());
    }

    @DisplayName("채널 전체 조회 테스트")
    @Test
    void testFindAllChannels() {
        // given
        channelService.createChannel("channel7", null);
        channelService.createChannel("channel8", null);

        // when
        var channels = channelService.findAllChannels();

        // then
        assertEquals(2, channels.size());
        assertEquals("channel7", channels.get(0).getName());
        assertEquals("channel8", channels.get(1).getName());
    }
}