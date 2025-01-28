package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserRepository {
    // Repository계층의 주요 역할이 데이터베이스 CRUD 작업처리
    // 라고하니깐 Repository만 만들도록 하자. Service와 Manager는 만들어도 감당 못하고 뭘 해야할지도 모르겠다.

    // 생성
    // 유저를 데이터 베이스에 저장.
    // 유저가 회원가입을 해서 정보를 서버를 넘기면 그 정보를 그대로 데이터베이스에 저장한다고 이해하자.
    void save(User user);

    // 조회
    // 관리자가 특정 유저를 찾는 기능이라고 생각하자.
    String findById(String id);
    void findAll();

    // 수정
    void update(String user);

    // 삭제
    void delete(String id);

}
