package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
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
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateFacadeImpl implements UserUpdateFacade {

  private final UserService userService;
  private final UserMapper userMapper;

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;
  @Override
  public CreateUserResponse updateUser(String userId, MultipartFile newProfile, UserUpdateDto updateDto) {

    log.info("[User Update] : 요청 수신={} , 수신 정보={}", userId, updateDto);
    User user = userService.updateUser(userId, updateDto);

    log.info("[User Update] : 기본 정보 업데이트 완료.");

    BinaryContent profile = null;

    if(newProfile != null && !newProfile.isEmpty()){

      log.info("[User Update] : 프로필 업데이트 시작");

      profile = binaryContentMapper.toProfileBinaryContent(newProfile, userId);

      user.updateProfileImage(profile.getId());
      binaryContentService.updateProfile(userId, profile);

      log.info("[User Update] : BinaryContent 저장 성공 ");

      // DB 사용시 삭제
      userService.update(user);
      log.info("[User Update] : user 프로필 업데이트 반영 성공");
    }

    log.info("[User Update] : user 업데이트 완료");
    return userMapper.toCreateUserResponse(user);
  }
}
