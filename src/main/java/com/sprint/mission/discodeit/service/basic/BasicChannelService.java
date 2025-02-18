package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    // 채널 생성
    @Override
    public void craete(Channel channel) {

        channelRepository.save(channel);
    }


    // 읽기
    // 채널 단건 조회
    @Override
    public Channel read(UUID id) {

        return channelRepository.load().get(id);
    }

    // 모든 유저의 모든 채널 반환
    @Override
    public List<Channel> readAll() {

        return changeList(channelRepository.load());
    }

    // 수정
    // 카테고리 수정
    @Override
    public void updateCategory(UUID id, String updateCategory) {

        Channel channel = read(id);

        channel.updateCategory(updateCategory);
        channelRepository.save(channel);
    }

    // 채널 이름 수정
    @Override
    public void updateName(UUID id, String updateName) {

        Channel channel = read(id);
        channel.updateName(updateName);
        channelRepository.save(channel);
    }

    // 채널 설명 수정
    @Override
    public void updateExplanation(UUID id, String updateExplanation) {

        Channel channel = read(id);
        channel.updateExplanation(updateExplanation);
        channelRepository.save(channel);
    }

    @Override
    public void addMember(UUID id, UUID memberId) {

        Channel channel = read(id);
        channel.addMember(memberId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteMember(UUID id, UUID memberId) {

        Channel channel = read(id);

        if (channel.getOwnerId() == memberId) {
            throw new IllegalArgumentException("채널주는 멤버에서 삭제할 수 없습니다.");
        }

        channel.deleteMember(memberId);
        channelRepository.save(channel);
    }

    // 채널 삭제
    @Override
    public void delete(UUID id) {

        channelRepository.delete(id);
    }

    // 채널 존재 여부 확인
    @Override
    public void channelIsExist(UUID id) {

        Map<UUID, Channel> channels = channelRepository.load();

        if (!channels.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }

    // 불러온 데이터 List로 변환
    private List<Channel> changeList(Map<UUID, Channel> map) {

        return map.values().stream().toList();
    }
}