package com.sprint.mission.discodeit.repository.file.json;

import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserRole;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JsonReadStatusRepositoryTest {
    ReadStatusRepository readStatusRepository = new JsonReadStatusRepository();

    User user = new User("userA", "email@gmail.com", "password", UserRole.ROLE_DEV);
    User user2 = new User("userB", "bbb@gmail.com", "password", UserRole.ROLE_COMMON);
    User user3 = new User("userC", "cccc@gmail.com", "password", UserRole.ROLE_ADMIN);
    Map<UUID, User> channelUsers = Map.of(user.getId(), user, user2.getId(), user2);
    Map<UUID, User> channelUsers2 = Map.of(user2.getId(), user2, user3.getId(), user3);

    Channel channel = new Channel("channelA", user, "PUBLIC", channelUsers);
    Channel channel2 = new Channel("channelB", user2, "PRIVATE", channelUsers2);
    ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId());
    ReadStatus readStatus2 = new ReadStatus(user2.getId(), channel2.getId());


    /**
     * 저장
     * 채널 아이디로 readStatus 찾기
     * 유저 이름으로 readStatus 찾기
     * readStatus 전체 찾기
     * 삭제
     */

    @Test
    @DisplayName("readStatus 저장")
    void save() {
        ReadStatus save = readStatusRepository.save(readStatus);

        assertNotNull(save);
    }

    @Test
    @DisplayName("채널 아이디로 readStatus 찾기")
    void findByChannelId() {
        ReadStatus save = readStatusRepository.save(readStatus);

        ReadStatus find = readStatusRepository.findByChannelId(channel.getId());

        assertEquals(save, find);
    }

    @Test
    @DisplayName("유저 이름으로 readStatus 찾기")
    void findByUserId() {
        ReadStatus save = readStatusRepository.save(readStatus);

        ReadStatus find = readStatusRepository.findByUserId(user.getId());

        System.out.println(save.getChannelId());
        System.out.println(find.getChannelId());

        System.out.println(save.getUserId());
        System.out.println(find.getUserId());

        assertEquals(save, find);
    }

    @Test
    @DisplayName("readStatus 전체 찾기")
    void findAll() {
        ReadStatus save1 = readStatusRepository.save(readStatus);
        ReadStatus save2 = readStatusRepository.save(readStatus2);

        Map<UUID, ReadStatus> all = readStatusRepository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("readStatus 삭제")
    void remove() {
        ReadStatus save = readStatusRepository.save(readStatus);

        readStatusRepository.remove(channel.getId());

        ReadStatus byChannelId = readStatusRepository.findByChannelId(channel.getId());
        Assertions.assertThat(byChannelId).isNull();
    }
}