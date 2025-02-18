package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요

    UUID create(MessageDto messageDto);

    Message findById(UUID id);

    List<Message> findAllByUserId(UUID userId);

    List<Message> findAllByChannelId(UUID channelId);

    List<Message> findAll();

    void update(MessageDto messageDto);

    void delete(UUID id);
}
