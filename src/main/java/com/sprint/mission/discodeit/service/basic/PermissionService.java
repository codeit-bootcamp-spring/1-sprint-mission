package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  public void checkIsAdminOrMe(UUID userId, UserDetails details) {
    if (!checkIsAdmin(details) && !checkIsMe(userId, details)) {
      throw new DiscodeitException(ErrorCode.ACCESS_DENIED);
    }
  }

  public void checkIsAdminOrAuthor(UUID messageId, UserDetails details) {
    if (!checkIsAdmin(details) && !checkIsAuthor(details, messageId)) {
      throw new DiscodeitException(ErrorCode.ACCESS_DENIED);
    }
  }

  public boolean checkIsAdmin(UserDetails userDetails) {
    if (userDetails instanceof DiscodeitUserDetails details) {
      return details.getUser().getRole() == UserRole.ROLE_ADMIN;
    }
    return false;
  }

  public boolean checkIsMe(UUID userId, UserDetails userDetails) {
    if (userDetails instanceof DiscodeitUserDetails details) {
      return userId.equals(details.getUser().getId());
    }
    return false;
  }

  public boolean checkIsAuthor(UserDetails userDetails, UUID messageId) {
    if (userDetails instanceof DiscodeitUserDetails details) {
      Message toEdit = messageRepository.findById(messageId)
          .orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

      if (!toEdit.getAuthor().getId().equals(details.getUser().getId())) {
        return false;
      }
      return true;
    }
    return false;
  }
}
