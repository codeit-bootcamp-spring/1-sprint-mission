package com.sprint.mission.discodeit.user.entity;

import java.util.Objects;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User extends BaseEntity {
	private String userid;
	private String password;
	private String username;
	private String email;

	public User(String userid, String password, String username, String email) {
		this.userid = userid;
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public void updateUsername(String username) {
		this.username = username;
	}

	public void updateUserEmail(String email) {
		this.email = email;
	}

	public void updateUserid(String userid) {
		this.userid = userid;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
			"id='" + getId() + '\'' +
			", userid='" + userid + '\'' +
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
