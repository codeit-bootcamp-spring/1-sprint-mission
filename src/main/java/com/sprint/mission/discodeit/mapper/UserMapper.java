package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserMapper {

  protected UserStatusRepository userStatusRepository;
  private BinaryContentMapper binaryContentMapper;

  // DTO 변환 로직 메서드
  /* TODO (toDto가 서비스 클래스 내부에 있었을 때): 이 클래스 내부에서만 쓰일 자체 메서드이니까 private 선언
      인터페이스는 외부와 상호작용할 메서드들(로미오 역할의 특징들-성격, 말투, 목소리 등. 이런거 줄리엣 역할도 알아야 연기를 같이 할 거 아냐~)의 나열(=public)이기 때문에 private은 인터페이스에 포함되지 않는다! 당연.
   */
  public UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(
            userStatus -> userStatus.isOnline())// UserStatus의 Optional 값이 존재하면 isOnline 메서드 호출-> Optional<Boolean> 반환
        .orElse(null);

    BinaryContentDto profile = binaryContentMapper.toDto(user.getProfile());

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profile,
        online
    );
  }
}
