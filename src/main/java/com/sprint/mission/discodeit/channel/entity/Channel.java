package com.sprint.mission.discodeit.channel.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Channel extends BaseEntity {
	private ChannelType type;
	private String name;
	private String description;

	public Channel(ChannelType type, String name, String description) {
		super();
		this.type = type;
		this.name = name;
		this.description = description;
	}

	public void update(String newName, String newDescription) {
		boolean anyValueUpdated = false;
		if (newName != null && !newName.equals(this.name)) {
			this.name = newName;
			anyValueUpdated = true;
		}
		if (newDescription != null && !newDescription.equals(this.description)) {
			this.description = newDescription;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updateTime();
		}
	}

}
