package some_path._1sprintmission.discodeit.service;

import some_path._1sprintmission.discodeit.dto.UserCreateRequestDTO;
import some_path._1sprintmission.discodeit.dto.UserDTO;
import some_path._1sprintmission.discodeit.dto.UserUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
public interface UserService {
    User create(UserCreateRequestDTO userCreateRequest);
    UserDTO find(UUID id);
    List<UserDTO> findAll();
    UserDTO update(UserUpdateRequestDTO request);
    void delete(UUID id);

    //디스코드 이름#+4자리 숫자, 동명이인을 구별하기 위한 방법으로 이름을 얻기 위해 userId 추가. 뒤에 4자리 난수 생성
    int makeDiscriminator(UUID userId);
}