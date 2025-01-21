package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JCFChannelRepositoryTest {

    ChannelRepository channelRepository = new JCFChannelRepository();


    @BeforeEach
    void init() {
        channelRepository = new JCFChannelRepository();
    }

    @Test
    @DisplayName("채널 저장")
    void save() {
        Channel channel = new Channel("newCh", new User("onwer", "dsadd@onwn.com", "owowowo"));

        Channel findChannel = channelRepository.saveChannel(channel);

        Assertions.assertEquals(channel, findChannel);
    }

    @Test
    @DisplayName("채널 단일 조회")
    void find() {
        Channel channel = new Channel("newCh", new User("onwer", "dsadd@onwn.com", "owowowo"));
        channelRepository.saveChannel(channel);

        Channel findChannel = channelRepository.findChannelById(channel.getChannelId());

        Assertions.assertEquals(channel, findChannel);
    }

    @Test
    @DisplayName("모든 채널 조회")
    void findALL() {
        Channel channel = new Channel("newCh", new User("onwer", "dsadd@onwn.com", "owowowo"));
        Channel channel2 = new Channel("newCh.2", new User("onwer2", "2222dsadd@onwn.com", "222owowowo"));
        Channel saveChannel = channelRepository.saveChannel(channel);
        Channel saveChannel2 = channelRepository.saveChannel(channel2);

        Map<UUID, Channel> testMap = new HashMap<>();
        testMap.put(saveChannel.getChannelId() ,saveChannel);
        testMap.put(saveChannel2.getChannelId() ,saveChannel2);

        Map<UUID, Channel> allChannel = channelRepository.findAllChannel();

        Assertions.assertEquals(testMap, allChannel);
    }

    @Test
    @DisplayName("채널 삭제")
    void delete() {
        Channel channel = new Channel("newCh", new User("onwer", "dsadd@onwn.com", "owowowo"));
        Channel channel2 = new Channel("newCh.2", new User("onwer2", "2222dsadd@onwn.com", "222owowowo"));
        Channel saveChannel = channelRepository.saveChannel(channel);
        Channel saveChannel2 = channelRepository.saveChannel(channel2);

        channelRepository.removeChannelById(channel.getChannelId());

        Assertions.assertEquals(null, channelRepository.findChannelById(channel.getChannelId()));
    }
}