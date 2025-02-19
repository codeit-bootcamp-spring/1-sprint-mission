package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.serial.FileChannelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileChannelRepositoryTest {

    ChannelRepository channelRepository = new FileChannelRepository();

    @Test
    @DisplayName("직렬화를 통한 채널 저장")
    void save() {
        Channel channel = new Channel("newName", new User("user", "user@nana.com", "dasdsdasdas"));

        Channel findChannel = channelRepository.saveChannel(channel);

        Assertions.assertEquals(channel, findChannel);
    }

    @Test
    @DisplayName("채널 단일 조회")
    void find() {
        Channel channel1 = new Channel("newName1", new User("user1", "user1@nana.com", "dasdsdasdas"));
        Channel channel2 = new Channel("newName2", new User("user2", "user2@nana.com", "dasdsdasdas"));
        channelRepository.saveChannel(channel1);
        channelRepository.saveChannel(channel2);

        Channel findChannel = channelRepository.findChannelById(channel1.getId());

        Assertions.assertEquals(channel1, findChannel);
    }

    @Test
    @DisplayName("모든 유저 조회")
    void findAll() {
        Channel channel1 = new Channel("newName1", new User("user1", "user1@nana.com", "dasdsdasdas"));
        Channel channel2 = new Channel("newName2", new User("user2", "user2@nana.com", "dasdsdasdas"));
        Map<UUID, Channel> channelMap = new HashMap<>();
        channelMap.put(channel1.getId(), channel1);
        channelMap.put(channel2.getId(), channel2);

        channelRepository.saveChannel(channel1);
        channelRepository.saveChannel(channel2);


        Map<UUID, Channel> allChannel = channelRepository.findAllChannel();

        Assertions.assertEquals(channelMap, allChannel);
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        Channel channel1 = new Channel("newName1", new User("user1", "user1@nana.com", "dasdsdasdas"));
        Channel channel2 = new Channel("newName2", new User("user2", "user2@nana.com", "dasdsdasdas"));
        channelRepository.saveChannel(channel1);
        channelRepository.saveChannel(channel2);

        channelRepository.removeChannelById(channel2.getId());

        assertNull(channelRepository.findChannelById(channel2.getId()));
    }

}