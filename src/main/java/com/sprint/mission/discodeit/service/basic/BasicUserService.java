package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final UserValidator userValidator;
  private final UserMapper userMapper;

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;


  @Override
  @Transactional
  public UserDto create(UserCreateDTO dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    BinaryContent nullableProfile = saveBinaryFile(optionalProfileCreateRequest);

    User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword(), nullableProfile);

    //cascade persist
    user.addUserStatus(new UserStatus(Instant.now()));
    User saveUser = userRepository.save(user);

    log.info("사용자 생성 완료 id: {}", saveUser.getId());

    return userMapper.toDto(saveUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID id) {
    User findUser = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    return userMapper.toDto(findUser);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID id, UserUpdateDTO dto,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    userValidator.validateUpdateUser(id, dto.getNewUsername(), dto.getNewEmail(),
        dto.getNewPassword());
    User findUser = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    BinaryContent nullableProfile = saveBinaryFile(optionalProfileCreateRequest);

    findUser.updateUser(dto.getNewUsername(), dto.getNewEmail(), dto.getNewPassword(),
        nullableProfile);

    log.info("사용자 수정 완료 id: {}", findUser.getId());

    return userMapper.toDto(findUser);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    //userStatus ddl on delete cascade, profile jpa delete cascade
    userRepository.deleteById(id);
    log.info("사용자 삭제 완료 id: {}", id);
  }

  @Transactional
  public BinaryContent saveBinaryFile(
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    return optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.getFileName();
          String contentType = profileRequest.getContentType();
          byte[] bytes = profileRequest.getBytes();

          BinaryContent binaryContent = binaryContentRepository.save(
              new BinaryContent(fileName, contentType, (long) bytes.length));

          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
  }

}