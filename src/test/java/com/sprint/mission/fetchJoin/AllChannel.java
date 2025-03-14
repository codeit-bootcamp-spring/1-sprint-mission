package com.sprint.mission.fetchJoin;

import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class AllChannel {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setting() {
        User user = new User("testUser", "testPassword", "testEmail", null);
        userRepository.save(user);
        Channel channel = new Channel("testChannel ", "testChannelName", ChannelType.PUBLIC);
        channelRepository.save(channel);
        readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
    }

    @Test
    @Transactional
    void testWrongAllChannel() {
        // Fetch all channels for the user
        List<User> userList = userRepository.findAll();
        System.out.println("첫번쨰 user의 ReadStatus " + userList.get(0).getReadStatus());
        userList.stream().forEach((user) -> {
            List<ReadStatus> readStatus = user.getReadStatus();
            readStatus.stream().forEach((status) -> {
                System.out.println("status = " + status);
                System.out.println("status의 채널 = " + status.getChannel());
            });
        });
    }

    @Test
    @Transactional
    void testAllChannel() {
        // Fetch all channels for the user
        em.flush();
        em.clear();
        List<User> userList = userRepository.findAll();
        System.out.println("찾은 userList = " + userList);

        UUID userId = userList.get(0).getId();
        List<ReadStatus> readStatusList = readStatusRepository.findAllByUserId(userId);
        readStatusList.stream().forEach(readStatus -> {
            System.out.println("readStatus = " + readStatus);
            System.out.println("readStatus.getUser() = " + readStatus.getUser());
            System.out.println("readStatus.getChannel() = " + readStatus.getChannel()); // 추가 쿼리 나가는 것 테스트
        });
    }

    @Test
    @Transactional
    void testAllChannelWithEntityGraph() {
        // Fetch all channels for the user
        em.flush();
        em.clear();
        User user = new User("또다른 유저", "또 다른 패스워드", "또 다른 이메일", null);
        userRepository.save(user);
        Channel channel = new Channel("testChannel ", "testChannelName", ChannelType.PUBLIC);
        channelRepository.save(channel);
        readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
        em.flush();
        em.clear();

        List<User> userList = userRepository.findAll();
        System.out.println("찾은 userList = " + userList);
        UUID userId = userList.get(0).getId();
        //List<ReadStatus> readStatusList = readStatusRepository.findAllByUserId(userId);
        List<ReadStatus> readStatusList = readStatusRepository.findPagingAllByUser_Id(userId);
        readStatusList.stream().forEach(readStatus -> {
            System.out.println("readStatus = " + readStatus);
            System.out.println("readStatus.getUser() = " + readStatus.getUser());
            System.out.println("readStatus.getChannel() = " + readStatus.getChannel()); // 추가 쿼리 나가는 것 테스트
        });
        assertThat(readStatusList.size()).isEqualTo(1);
    }
}