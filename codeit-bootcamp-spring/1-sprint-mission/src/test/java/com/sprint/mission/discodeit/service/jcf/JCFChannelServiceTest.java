package com.sprint.mission.discodeit.service.jcf;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.service.channel.ChannelConverter;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class JCFChannelServiceTest {
    private ChannelService channelService;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;

    private ChannelConverter channelConverter = new ChannelConverter();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        channelService = JCFChannelService.getInstance(userRepository, channelRepository);
    }


    // createChannelOrThrow 테스트 => uuid 값이 수정할 수 없어서 테스트하는데 너무 어려움을 느낌..
    @Test
    void test() {
        // given
        var user = User.createFrom("SB_1기_백재우");
        var channelName = "스프링백엔드_1기";
        var channel = Channel.createOfChannelNameAndUser(channelName, user);
        // when
        var request = new CreateNewChannelRequest(user.getId(), channelName);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // then
        channelService.createChannelOrThrow(request);

        verify(userRepository, times(1)).findById(user.getId());
    }
}