package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.view.ConsoleView;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final ConsoleView consoleView;

    @Override
    public ChannelResponse createChannel(ChannelCreateRequest request) {
        Channel channel = new Channel(
                request.type(),
                request.channelName(),
                request.description()
        );
        channelRepository.save(channel);
        consoleView.displaySuccess(channel.getChannelName() + " 채널이 생성되었습니다. 타입: " + request.type());

        return ChannelResponse.from(channel);
    }

    @Override
    public ChannelResponse createPublicChannel(String name, String description) {
        return createChannel(ChannelCreateRequest.publicChannel(name, description));
    }

    @Override
    public ChannelResponse createChannel(String name, String description, ChannelType type) {
        return createChannel(new ChannelCreateRequest(name, description, type));
    }

    @Override
    public ChannelResponse getChannelById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel != null) {
            consoleView.displayChannel(channel, null);
            return ChannelResponse.from(channel);
        }
        consoleView.displayError("채널을 찾을 수 없습니다");
        return null;
    }

    @Override
    public List<ChannelResponse> getAllChannels() {
        List<Channel> channels = channelRepository.findAll();
        consoleView.displayChannels(channels);

        List<ChannelResponse> channelResponses = new ArrayList<>();
        for (Channel channel : channels) {
            channelResponses.add(ChannelResponse.from(channel));
        }

        return channelResponses;
    }

    @Override
    public ChannelResponse updateChannel(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(channelId);
        if (channel != null) {
            if (request.updateChannelName() != null && !request.updateChannelName().isBlank()) {
                channel.updateChannelName(request.updateChannelName());
                consoleView.displaySuccess("채널 이름이 업데이트되었습니다: " + request.updateChannelName());
            }

            if (request.updateChannelDescription() != null) {
                channel.updateDescription(request.updateChannelDescription());
                consoleView.displaySuccess("채널 설명이 업데이트되었습니다");
            }

            channelRepository.save(channel);
            return ChannelResponse.from(channel);
        } else {
            consoleView.displayError("채널을 찾을 수 없습니다");
            return null;
        }
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel != null) {
            messageRepository.deleteAllByChannelId(channelId);
            readStatusRepository.deleteAllByChannelId(channelId);

            channelRepository.deleteById(channelId);
            consoleView.displaySuccess("채널이 삭제되었습니다");
            return true;
        }
        consoleView.displayError("채널 삭제에 실패했습니다");
        return false;
    }
}