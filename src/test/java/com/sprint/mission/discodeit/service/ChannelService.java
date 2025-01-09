package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    //생성
    void creatChannel(Channel channel);

    //읽기
    Optional<Channel> getChannelById(UUID id);

    //모두 읽기
    List<Channel> getAllUsers();

    //수정
    void updateUser(UUID id,Channel updatedChannel);

    //삭제
    void deleteUser(UUID id);
}
