package com.sprint.mission.discodeit.service.file;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.Optional;

class FileChannelServiceTest {
    private static final String TEST_FILE_PATH = "data/test_channels.dat";

    private FileChannelService fileChannelService;

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            assertTrue(file.delete(), "테스트 파일 삭제 실패");
        }

        fileChannelService = new FileChannelService(TEST_FILE_PATH);
    }

    @Test
    @DisplayName("채널을 생성한다.")
    void testCreateChannel() {
        // given
        Channel channel = fileChannelService.createChannel("testChannel");

        // when
        Map<UUID, Channel> channels = fileChannelService.getChannels();

        // then
        assertNotNull(channel);
        assertEquals("testChannel", channel.getChannelName());
        assertEquals(1, channels.size());
        assertTrue(channels.containsKey(channel.getId()));
    }

    @Test
    @DisplayName("채널을 생성하고 단일 조회를 한다")
    void testGetChannel() {
        // given
        Channel channel = fileChannelService.createChannel("testChannel");

        // when
        Optional<Channel> foundChannel = fileChannelService.getChannel(channel.getId());

        // then
        assertTrue(foundChannel.isPresent());
        assertEquals("testChannel", foundChannel.get().getChannelName());
    }

    @Test
    @DisplayName("채널을 생성하고 메시지를 추가한 후 확인한다")
    void testAddMessageToChannel() {
        // given
        Channel channel = fileChannelService.createChannel("testChannel");
        UUID messageUUID = UUID.randomUUID();

        // when
        Optional<Channel> updatedChannel = fileChannelService.addMessageToChannel(channel.getId(), messageUUID);

        // then
        assertTrue(updatedChannel.isPresent());
        assertEquals(updatedChannel.get().getMessageList().getFirst(), messageUUID);
    }

    @Test
    @DisplayName("채널을 생성하고 이름을 업데이트 후 확인한다")
    void testUpdateChannel() {
        // given
        Channel channel = fileChannelService.createChannel("testChannel");

        // when
        Optional<Channel> updatedChannel = fileChannelService.updateChannel(channel.getId(), "updatedChannel");

        // then
        assertTrue(updatedChannel.isPresent());
        assertEquals("updatedChannel", updatedChannel.get().getChannelName());

        Optional<Channel> foundChannel = fileChannelService.getChannel(channel.getId());
        assertTrue(foundChannel.isPresent());
        assertEquals("updatedChannel", foundChannel.get().getChannelName());
    }

    @Test
    @DisplayName("채널을 생성하고 삭제한 후 확인한다")
    void testDeleteChannel() {
        // given
        Channel channel = fileChannelService.createChannel("testChannel");

        // when
        Optional<Channel> deletedChannel = fileChannelService.deleteChannel(channel.getId());

        // then
        assertTrue(deletedChannel.isPresent());
        assertEquals("testChannel", deletedChannel.get().getChannelName());

        Optional<Channel> foundChannel = fileChannelService.getChannel(channel.getId());
        assertFalse(foundChannel.isPresent());

        Map<UUID, Channel> channels = fileChannelService.getChannels();
        assertTrue(channels.isEmpty());
    }

    @Test
    @DisplayName("채널을 생성하고 새로운 서비스를 만들 때 로드가 되는지 확인한다")
    void testPersistenceAcrossInstances() {
        // given
        Channel channel = fileChannelService.createChannel("persistentChannel");

        // when
        FileChannelService newServiceInstance = new FileChannelService(TEST_FILE_PATH);

        // then: 채널 정보가 새로운 인스턴스에서도 정상적으로 조회되는지 확인
        Optional<Channel> foundChannel = newServiceInstance.getChannel(channel.getId());
        assertTrue(foundChannel.isPresent());
        assertEquals("persistentChannel", foundChannel.get().getChannelName());
    }
}
