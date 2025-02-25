package com.sprint.mission.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;


class ChannelServiceTest {
//    @Mock
//    private ChannelRepository channelRepository;
//
//    @InjectMocks
//    private BasicChannelService channelService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("채널을 생성한다.")
//    void testCreateChannel() {
//        // given
//        String channelName = "channel1";
//        CreateChannelRequest request = new CreateChannelRequest(channelName);
//
//        // when
//        ChannelResponse response = channelService.createChannel(request);
//
//        // then
//        assertNotNull(response);
//        assertEquals(channelName, response.channel());
//    }
//
//    @Test
//    @DisplayName("채널을 생성하고 단일 조회를 한다")
//    void testGetChannel() {
//        // given
//        String channelName = "channel1";
//        CreateChannelRequest request = new CreateChannelRequest(channelName);
//        Channel channel = new Channel(request.channelName());
//
//        // when
//        when(channelRepository.getChannelById(any())).thenReturn(Optional.of(channel));
//        ChannelResponse response = channelService.createChannel(request);
//        Optional<ChannelResponse> findChannel = channelService.getChannel(channel.getId());
//
//        // then
//        assertTrue(findChannel.isPresent());
//        assertEquals(channelName, findChannel.get().channel());
//    }
//
//    @Test
//    @DisplayName("채널을 생성하고 메시지를 추가한 후 확인한다")
//    void testAddMessageToChannel() {
//        // given
//        String channelName = "channel1";
//        CreateChannelRequest request = new CreateChannelRequest(channelName);
//        Channel channel = new Channel(request.channelName());
//
//        String userName1 = "username1";
//        String userEmail1 = "email1@email.com";
//        String password1 = "password1";
//        User user1 = new User(userName1, userEmail1, password1);
//
//        String message1 = "message1";
//
//        Message newMessage = new Message(message1, user1.getId(), channel.getId());
//
//        // when
//        when(channelRepository.getChannelById(any())).thenReturn(Optional.of(channel));
//        ChannelResponse response = channelService.createChannel(request);
//
//        // then
//        assertEquals(channelName, response.channel());
//        assertEquals(0, channel.getMessageList().size());
//
//        // when
//        Optional<ChannelResponse> addResponse = channelService.addMessageToChannel(newMessage.getId(), channel.getId());
//
//        // then
//        assertTrue(addResponse.isPresent());
//        assertEquals(channelName, addResponse.get().channel());
//        assertEquals(1, channel.getMessageList().size());
//
//    }
//
//    @Test
//    @DisplayName("채널을 생성하고 이름을 업데이트 후 확인한다")
//    void testUpdateChannel() {
//        // given
//        String channelName = "channel1";
//        CreateChannelRequest request = new CreateChannelRequest(channelName);
//        Channel channel = new Channel(request.channelName());
//
//        // when
//        when(channelRepository.getChannelById(any())).thenReturn(Optional.of(channel));
//        ChannelResponse response = channelService.createChannel(request);
//
//        // then
//        assertEquals(channelName, response.channel());
//
//        // when
//        String updateChannelName = "updateChannel1";
//        Optional<ChannelResponse> updateResponse = channelService.updateChannel(channel.getId(), updateChannelName);
//
//        // then
//        assertTrue(updateResponse.isPresent());
//        assertEquals(updateChannelName, updateResponse.get().channel());
//    }
//
//    @Test
//    @DisplayName("채널을 생성하고 삭제한 후 확인한다")
//    void testDeleteChannel() {
//        // given
//        String channelName = "channel1";
//        CreateChannelRequest request = new CreateChannelRequest(channelName);
//        Channel channel = new Channel(request.channelName());
//
//        // when
//        when(channelRepository.getChannelById(any())).thenReturn(Optional.of(channel));
//        ChannelResponse response = channelService.createChannel(request);
//        Optional<ChannelResponse> channelResponse = channelService.getChannel(channel.getId());
//
//        // then
//        assertTrue(channelResponse.isPresent());
//        assertEquals(channelName, channelResponse.get().channel());
//
//        // when
//        when(channelRepository.getChannelById(any())).thenReturn(Optional.empty());
//        channelService.deleteChannel(channel.getId());
//
//        // then
//        Optional<ChannelResponse> deletedChannel = channelService.getChannel(channel.getId());
//        assertTrue(deletedChannel.isEmpty());
//    }
}
