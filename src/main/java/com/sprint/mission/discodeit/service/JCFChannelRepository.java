package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public interface JCFChannelRepository {
    // Repository계층의 주요 역할이 데이터베이스 CRUD 작업처리
    // 라고하니깐 Repository만 만들도록 하자. Service와 Manager는 만들어도 감당 못하고 뭘 해야할지도 모르겠다.

    // 생성
    void save(Channel channel);

    // 조회
    void findById(UUID id);
    void findAll();

    // 수정
    void update(Channel channel);

    // 삭제
    void delete(UUID id);
}
