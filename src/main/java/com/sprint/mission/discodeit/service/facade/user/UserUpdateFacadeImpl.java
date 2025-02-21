package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateFacadeImpl implements UserUpdateFacade {

  private final UserService userService;
  private final UserMapper userMapper;

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;
  @Override
  public UserResponseDto updateUser(String userId, UserUpdateDto updateDto) {

    log.info("[User Update] : 요청 수신={} , 수신 정보={}", userId, updateDto);
    User user = userService.updateUser(userId, updateDto, updateDto.inputPassword());

    log.info("[User Update] : 기본 정보 업데이트 완료.");

    BinaryContent profile = null;
    if(updateDto.profileImage() != null && !updateDto.profileImage().isEmpty()){

      log.info("[User Update] : 프로필 업데이트 시작");
      profile = binaryContentMapper.toProfileBinaryContent(updateDto.profileImage(), userId);
      user.updateProfileImage(profile.getUUID());
      binaryContentService.updateProfile(userId, profile);

      log.info("[User Update] : BinaryContent 저장 성공 = {}", profile);

      // DB 사용시 삭제
      userService.update(user);
      log.info("[User Update] : user 프로필 업데이트 반영 성공");
    }

    log.info("[User Update] : user 업데이트 완료");
    return userMapper.toDto(user);
  }
}
