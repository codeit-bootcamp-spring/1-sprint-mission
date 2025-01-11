package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.service.channel.ChannelService;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    // 저장
    public void createChannel(CreateNewChannelRequest request) {
        // 회원 찾기

        // 회원의 채널 생성 메소드 호출

        // 생성된 채널 DB에 저장
    }

    // 수정

    // 삭제

    /**
     *  읽기
     *   - 단건
     *   - 복수건
     */
}
