package com.sprint.mission.discodeit.dto.user;

import org.springframework.web.multipart.MultipartFile;

public record CreateUserRequestDto(String email, String password, String name
        , String nickname, String phoneNumber, MultipartFile profileImageFile) {

    // record
    // 필드 유형과 이름만 필요한 불변 데이터 클래스
    // equals, hashcode, toString 메서드와 private, final field, public constructor
    // -> Java 컴파일러에 의해 생성됨
}