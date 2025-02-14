package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.PublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public ChannelResponse create(ChannelRequest request) {
        if(request.channelType() == ChannelType.Private){
            PrivateChannelRequest privateChannel = new PrivateChannelRequest(request.member(), request.owner(), request.channelType());
            privateChannelCreate(privateChannel);
        }else{
            PublicChannelRequest publicChannel = new PublicChannelRequest(request.name(), request.description(), request.owner(), request.channelType());
            publicChannelCreate(publicChannel);
        }

        return null;
    }

    private ChannelResponse privateChannelCreate(PrivateChannelRequest request){
        Channel channel = new Channel(
                null,
                null,
                request.member(),
                request.owner(),
                request.channelType()
        );
        channelRepository.save(channel);
        return ChannelResponse.fromEntity(channel);
    }

    private ChannelResponse publicChannelCreate(PublicChannelRequest request){
        Channel channel = new Channel(
                request.name(),
                request.description(),
                null,
                request.owner(),
                request.channelType()
        );
        channelRepository.save(channel);
        return ChannelResponse.fromEntity(channel);
    }


    @Override
    public ChannelResponse readOne(UUID id) {
        Channel channel = channelRepository.findById(id);

        return ChannelResponse.fromEntity(channel);
    }

    @Override
    public List<ChannelResponse> readAll() {
        List<Channel> channels = channelRepository.readAll();
        List<ChannelResponse> responses = channels.stream()
                .map(channel -> ChannelResponse.fromEntity(channel))
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest updateChannel) {
        if(updateChannel.channelType() == ChannelType.Private) {
            System.out.println("Private Channel은 수정할수 없습니다.");
            return null;
        }
        Channel channel = channelRepository.modify(id, new Channel(
                updateChannel.name(),
                updateChannel.description(),
                updateChannel.member(),
                updateChannel.owner(),
                updateChannel.channelType()
        ));
        return ChannelResponse.fromEntity(channel);
    }

    @Override
    public boolean delete(UUID id) {
        return channelRepository.deleteById(id);
    }
}
