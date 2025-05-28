package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.io.Serializable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {

    if (authentication == null || targetDomainObject == null) {
      return false;
    }

    //메세지
    if (targetDomainObject instanceof Message message) {
      return checkMessagePermission(authentication, message, permission.toString());
    }

    //읽기 권한
    if (targetDomainObject instanceof ReadStatus readStatus) {
      return checkReadStatusPermission(authentication, readStatus, permission.toString());
    }

    //사용자 권한
    if (targetDomainObject instanceof User user) {
      return checkUserPermission(authentication, user, permission.toString());
    }

    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    if (authentication == null || targetId == null || targetType == null) {
      return false;
    }

    return switch (targetType) {
      case "Message" -> checkMessagePermissionById(authentication, targetId, permission.toString());
      case "User" -> checkUserPermissionById(authentication, targetId, permission.toString());
      case "ReadStatus" ->
          checkReadStatusPermissionById(authentication, targetId, permission.toString());
      default -> false;
    };
  }

  private boolean checkUserPermission(Authentication authentication, User user, String permission) {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return switch (permission.toUpperCase()) {
      case "UPDATE", "DELETE" -> {
        //사용자 본인이거나 Admin인 경우만 가능
        boolean isSelf = user.getId().equals(currentUserId);
        boolean isAdmin = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        yield isSelf || isAdmin;
      }
      default -> false;
    };
  }

  private boolean checkUserPermissionById(Authentication authentication, Serializable targetId,
      String permission) {
    UUID userId = UUID.fromString(targetId.toString());
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return switch (permission.toUpperCase()) {
      case "UPDATE", "DELETE" -> {
        //사용자 본인이거나 Admin인 경우만 가능
        boolean isSelf = userId.equals(currentUserId);
        boolean isAdmin = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        yield isSelf || isAdmin;
      }
      default -> false;
    };

  }

  private boolean checkReadStatusPermissionById(Authentication authentication,
      Serializable targetId, String permission) {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return switch (permission.toUpperCase()) {
      case "UPDATE" -> {
        // UPDATE의 경우 targetId는 readStatusId
        UUID readStatusId = UUID.fromString(targetId.toString());
        yield readStatusRepository.existsByIdAndUserId(readStatusId, currentUserId);
      }
      case "UPDATE_CHANNEL" -> {
        // UPDATE_CHANNEL의 경우 targetId는 channelId
        UUID channelId = UUID.fromString(targetId.toString());
        yield readStatusRepository.existsByChannelIdAndUserId(channelId, currentUserId);
      }
      default -> false;
    };
  }


  //readStatus 권한 확인
  private boolean checkReadStatusPermission(Authentication authentication, ReadStatus readStatus,
      String permission) {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return switch (permission.toUpperCase()) {
      case "CREATE", "UPDATE" -> // 생성, 수정은 본인만 가능
          readStatus.getUser().getId().equals(currentUserId);
      default -> false;
    };
  }

  //Message 객체가 있을때 사용하는 메서드
  private boolean checkMessagePermission(Authentication authentication, Message message,
      String permission) {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    String currentUsername = userDetails.getUsername();

    return switch (permission.toUpperCase()) {
      case "READ" -> true; //모든 인증된 사용자는 읽기 가능
      case "WRITE", "DELETE" -> {
        // 메세지 작성자 본인 이거나 ADMIN인 경우
        boolean isAuthor = message.getAuthor().getUsername().equals(currentUsername);
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        yield isAdmin || isAuthor;
      }
      //모든 케이스에 속하지 않는다면 false
      default -> false;
    };
  }

  //Message 객체가 없이 Message id만 있을 때 사용
  private boolean checkMessagePermissionById(Authentication authentication, Serializable targetId,
      String permission) {
    UUID messageId = UUID.fromString(targetId.toString());
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    return switch (permission.toUpperCase()) {
      case "READ" -> messageRepository.existsById(messageId); // 메시지가 존재하면 읽기 가능
      case "WRITE", "DELETE" -> {
        // DB에서 작성자 확인
        boolean isAuthor = messageRepository.existsByIdAndAuthor_Id(messageId,
            currentUserId);
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        yield isAuthor || isAdmin;
      }
      default -> false;
    };
  }
}
