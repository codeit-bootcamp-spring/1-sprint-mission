package com.sprint.mission.discodeit.user.entity;

import java.util.Objects;
import java.util.UUID;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User extends BaseEntity {
	private String username;
	private String email;
	private String password;
	private UUID profileId;

	public User(String username, String email, String password, UUID profileId) {
		super();
		this.password = password;
		this.username = username;
		this.email = email;
		this.profileId = profileId;
	}

	public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
		boolean anyValueUpdated = false;
		if (newUsername != null && !newUsername.equals(this.username)) {
			this.username = newUsername;
			anyValueUpdated = true;
		}
		if (newEmail != null && !newEmail.equals(this.email)) {
			this.email = newEmail;
			anyValueUpdated = true;
		}
		if (newPassword != null && !newPassword.equals(this.password)) {
			this.password = newPassword;
			anyValueUpdated = true;
		}
		if (newProfileId != null && !newProfileId.equals(this.profileId)) {
			this.profileId = newProfileId;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updateTime();
		}
	}

	@Override
	public String toString() {
		return "User{" +
			"id='" + getId() + '\'' +
			", username='" + username + '\'' +
			", email='" + email + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return getId().equals(user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
