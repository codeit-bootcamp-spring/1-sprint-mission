package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
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
//        ChannelResponse response;
//        if(request.channelType() == Channel.ChannelType.Private){
//            PrivateChannelRequest privateChannel = new PrivateChannelRequest(request.member(), request.owner(), request.channelType());
//            response = privateChannelCreate(privateChannel);
//        }else{
//            PublicChannelRequest publicChannel = new PublicChannelRequest(request.name(), request.description(), request.owner(), request.channelType());
//            response = publicChannelCreate(publicChannel);
//        }

        Channel channel = new Channel(request.name(), request.description(), request.channelType());
        channelRepository.save(channel);
        return ChannelResponse.fromEntity(channel);
    }

//    private ChannelResponse privateChannelCreate(PrivateChannelRequest request){
//        Channel channel = new Channel(
//                null,
//                null,
//                request.member(),
//                request.owner(),
//                request.channelType()
//        );
//        channelRepository.save(channel);
//        return ChannelResponse.fromEntity(channel);
//    }
//
//    private ChannelResponse publicChannelCreate(PublicChannelRequest request){
//        Channel channel = new Channel(
//                request.name(),
//                request.description(),
//                null,
//                request.owner(),
//                request.channelType()
//        );
//        channelRepository.save(channel);
//        return ChannelResponse.fromEntity(channel);
//    }


    @Override
    public ChannelResponse readOne(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다. : " + id));

        return ChannelResponse.fromEntity(channel);
    }

    @Override
    public List<ChannelResponse> readAll() {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelResponse> responses = channels.stream()
                .map(channel -> ChannelResponse.fromEntity(channel))
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<ChannelResponse> publicChannelReadAll() {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelResponse> responses = channels.stream()
                .filter(channel -> channel.getChannelType().equals(Channel.ChannelType.Public))
                .map(channel -> ChannelResponse.fromEntity(channel))
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public ChannelResponse publicChannelReadOne(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다. : " + id));

        if(channel.getChannelType().equals(Channel.ChannelType.Public)){
            return ChannelResponse.fromEntity(channel);
        }
        return null;
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest updateChannel) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다. : " + id));

        if(channel.isPrivate()) {
            System.out.println("Private Channel은 수정할수 없습니다.");
            return null;
        }

        channel.setName(updateChannel.name());
        channel.setDescription(updateChannel.description());

        channelRepository.save(channel);

        return ChannelResponse.fromEntity(channel);
    }

    @Override
    public boolean delete(UUID id) {
        if(!channelRepository.existsById(id)){
            throw new ResourceNotFoundException("없거나, 삭제된 아이디 입니다. : " + id);
        }

        channelRepository.deleteById(id);
        return true;
    }
}
