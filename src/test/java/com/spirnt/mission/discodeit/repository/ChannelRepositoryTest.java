package com.spirnt.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("공개 채널 가져오기 테스트")
    void testFindAllPublic() {
        // when
        List<Channel> publicChannels = channelRepository.findAllPublic();

        // then
        // 공개 채널만 가져왔어야 함
        for (Channel ch : publicChannels) {
            assertEquals(ChannelType.PUBLIC, ch.getType());
        }
    }
}
