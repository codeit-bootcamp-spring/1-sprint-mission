package com.sprint.mission.discodeit.user.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.entity.UserStatus;

@Component
public class UserMapper {
	public UserResponse userToUserResponse(User user, UserStatus userStatus) {
		return new UserResponse(
			user.getId(),
			user.getUserid(),
			user.getUsername(),
			user.getEmail(),
			userStatus.getStatusType(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);
	}

	public List<UserResponse> userListToUserResponseList(List<User> users, List<UserStatus> statuses) {
		Map<UUID, UserStatus> statusMap = new HashMap<>();
		for (UserStatus status : statuses) {
			statusMap.put(status.getUserId(), status);
		}

		List<UserResponse> responses = new ArrayList<>();
		for (User user : users) {
			UserStatus status = statusMap.get(user.getId());
			if (status == null) {
				throw new IllegalStateException("UserStatus not found for user: " + user.getId());
			}
			responses.add(userToUserResponse(user, status));
		}
		return responses;
	}

}
