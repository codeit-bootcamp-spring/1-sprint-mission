package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.user.UserErrorMessage.*;

import com.sprint.mission.discodeit.common.error.user.UserErrorMessage;
import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.util.Optional;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public JCFChannelService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createChannel(CreateNewChannelRequest request) {
        var findUser = userRepository.findById(request.userId())
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        var createdChannel = findUser.createChannel(request.channelName());

        channelRepository.save(createdChannel);
        userRepository.save(findUser);
    }

    // 수정

    // 삭제

    /**
     *  읽기
     *   - 단건
     *   - 복수건
     */
}
