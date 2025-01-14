package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.common.error.ErrorMessage.USER_NOT_FOUND;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.dto.ChangeChannelNameRequest;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public JCFChannelService(
            ChannelRepository channelRepository,
            UserRepository userRepository
    ) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createChannelOrThrow(CreateNewChannelRequest request) {

        var findUser = findUserByIdOrThrow(request.userId());

        var createdChannel = findUser.createNewChannel(request.channelName());

        channelRepository.save(createdChannel);
        userRepository.save(findUser);
    }

    @Override
    public void changeChannelNameOrThrow(ChangeChannelNameRequest request) {
        var foundUser = findUserByIdOrThrow(request.userId());

        var changedChannel = foundUser.changeChannelName(request.channelId(), request.newChannelName());

        channelRepository.save(changedChannel);
        userRepository.save(foundUser);
    }

    // 삭제


    @Override
    public void deleteUserParticipatedChannelByChannelId(String channelId) {

    }

    /**
     * 읽기 - 단건 - 복수건
     */

    private User findUserByIdOrThrow(UUID id) {
        var foundUser = userRepository.findById(id)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        return foundUser;
    }
}
