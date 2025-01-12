package com.sprint.mission.discodeit.service.jcf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import org.junit.jupiter.api.BeforeEach;

class JCFChannelServiceTest {

    private ChannelService channelService;

    private ChannelRepository channelRepository;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        channelRepository = mock(ChannelRepository.class);
        userRepository = mock(UserRepository.class);

        channelService = spy(new JCFChannelService(channelRepository, userRepository));
    }
}