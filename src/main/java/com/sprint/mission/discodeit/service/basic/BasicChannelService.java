package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.*;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

//        Channel channel = new Channel(request.name(), request.description(), request.channelType());
        log.debug("채널 생성 시도 - request : {}", request);
        Channel channel = ChannelMapper.INSTANCE.toEntity(request);

        channelRepository.save(channel);

        log.info("채널 생성 완료 - channel : {}", channel);
        return ChannelMapper.INSTANCE.toDto(channel);
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
        log.debug("특정 채널 조회 시도 - id : {}", id);
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("틀정 채널 조회 실패 - 저장되지 않았거나, 삭제된 아이디 : {}", id);
                    return new ChannelNotFoundException(id);
                });

        log.info("특정 채널 조회 성공 - id : {}", id);
        return ChannelMapper.INSTANCE.toDto(channel);
    }

    @Override
    public List<ChannelResponse> readAll() {
        log.debug("채널 전체 조회 요청");

        List<Channel> channels = channelRepository.findAll();
        log.debug("조회된 채널 개수 : {}", channels.size());

        List<ChannelResponse> responses = channels.stream()
                .map(channel -> ChannelMapper.INSTANCE.toDto(channel))
                .collect(Collectors.toList());

        log.info("채널 조회 완료 - 총 {}개", responses.size());
        return responses;
    }

    @Override
    public List<ChannelResponse> publicChannelReadAll() {
        log.debug("공개 채널 전체 조회 요청");

        List<Channel> channels = channelRepository.findAll();
        log.debug("조회된 채널 개수 : {}", channels.size());

        List<ChannelResponse> responses = channels.stream()
                .filter(channel -> channel.getChannelType().equals(Channel.ChannelType.Public))
                .map(channel -> ChannelMapper.INSTANCE.toDto(channel))
                .collect(Collectors.toList());

        log.info("채널 조회 완료 - 총 {}개", responses.size());
        return responses;
    }

    @Override
    public ChannelResponse publicChannelReadOne(UUID id) {
        log.debug("공개 채널 단건 조회 요청 - id : {}", id);

        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("채널 조회 실패 - 존재하지 않음 (ID: {})", id);
                    return new ChannelNotFoundException(id);
                });

        if (channel.getChannelType() != Channel.ChannelType.Public) {
            log.warn("비공개 채널에 대한 접근 시도 - ID: {}", id);
            throw new IllegalArgumentException("비공개 채널은 접근할 수 없습니다.");
        }

        log.info("공개 채널 조회 성공 - ID: {}", id);
        return ChannelMapper.INSTANCE.toDto(channel);
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest updateChannel) {
        log.debug("채널 수정 요청 - id: {}, updateChannel: {}", id, updateChannel);
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("채널 조회 실패 - 저장되지 않았거나, 삭제된 id: {}", id);
                    return new ChannelNotFoundException(id);
                });

        if(channel.isPrivate()) {
            log.warn("비공개 채널 수정 시도 싪패 - ID: {}",id);
            return null;
        }

        channel.setName(updateChannel.name());
        channel.setDescription(updateChannel.description());

        channelRepository.save(channel);

        log.info("채널 수정 성공 - id: {}", id);
        return ChannelMapper.INSTANCE.toDto(channel);
    }

    @Override
    public boolean delete(UUID id) {
        log.debug("채널 삭제 요청 - id: {}", id);

        if(!channelRepository.existsById(id)){
            log.warn("채널 삭제 실패 - 없거나 삭제된 ID : {}", id);
            throw new ChannelNotFoundException(id);
        }

        channelRepository.deleteById(id);
        log.info("채널 삭제 완료 - id : {}", id);
        return true;
    }
}
