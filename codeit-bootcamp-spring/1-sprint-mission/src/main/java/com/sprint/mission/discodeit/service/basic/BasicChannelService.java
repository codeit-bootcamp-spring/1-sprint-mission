package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.common.error.ErrorMessage.CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.common.error.ErrorMessage.USER_NOT_FOUND;

import com.sprint.mission.discodeit.common.error.channel.ChannelException;
import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.entity.channel.dto.ChangeChannelNameRequest;
import com.sprint.mission.discodeit.entity.channel.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.channel.dto.DeleteChannelRequest;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.repository.jcf.channel.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.service.channel.ChannelConverter;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelConverter channelConverter;

    public static ChannelService getInstance(UserRepository userRepository, ChannelRepository channelRepository) {
        return new BasicChannelService(channelRepository, userRepository, new ChannelConverter());
    }

    @Override
    public ChannelResponse createChannelOrThrow(CreateNewChannelRequest request) {
        /**
         * ==> 코드리뷰 : 메서드 내부에 다른 메서드를 호출하면서 throw가 발생가능하다는 것을 코드 블록의 메서드의 이름에 추가해야할까요?
         * createChannel VS createChannelOrThrow
         */
        var findUser = findUserByIdOrThrow(request.userId());

        var createdChannel = findUser.openNewChannel(request.channelName());

        channelRepository.save(createdChannel);
        userRepository.save(findUser);

        return channelConverter.toDto(createdChannel);
    }

    @Override
    public void changeChannelNameOrThrow(ChangeChannelNameRequest request) {
        var foundUser = findUserByIdOrThrow(request.userId());

        var changedChannel = foundUser.changeChannelName(request.channelId(), request.newChannelName());

        channelRepository.save(changedChannel);
        userRepository.save(foundUser);
    }

    @Override
    public void deleteChannelByChannelIdOrThrow(DeleteChannelRequest request) {
        var foundChannel = channelRepository.findById(request.channelId())
                .orElseThrow(
                        () -> ChannelException.ofErrorMessageAndNotExistChannelId(
                                CHANNEL_NOT_FOUND,
                                request.channelId())
                );

        var foundUser = findUserByIdOrThrow(request.userId());
        foundChannel.deleteChannel(foundUser);

        channelRepository.save(foundChannel);
    }

    private User findUserByIdOrThrow(UUID id) {
        var foundUser = userRepository.findById(id)
                .filter(User::isNotUnregistered)
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        return foundUser;
    }
}
