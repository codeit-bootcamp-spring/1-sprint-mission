package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

	@Autowired
	private ChannelRepository channelRepository;

	@Test
	@Sql(scripts = {"/channel/channels.sql"})
	void 채널_이름으로_정상적으로_조회된다() {
		// given
		String channelName = "General";

		// when
		Optional<Channel> channel = channelRepository.findByName(channelName);

		// then
		assertThat(channel).isPresent();
		assertThat(channel.get().getName()).isEqualTo(channelName);
	}

	@Test
	@Sql(scripts = {"/channel/channels.sql"})
	void 특정_타입의_채널을_조회할_수_있다() {
		// when
		List<Channel> publicChannels = channelRepository.findByType(ChannelType.PUBLIC);

		// then
		assertThat(publicChannels).isNotEmpty();
		assertThat(publicChannels.get(0).getType()).isEqualTo(ChannelType.PUBLIC);
	}

	@Test
	@Sql(scripts = {"/channel/channels.sql"})
	void 존재하지_않는_채널을_조회하면_빈값을_반환한다() {
		// given
		String channelName = "NonExistent";

		// when
		Optional<Channel> channel = channelRepository.findByName(channelName);

		// then
		assertThat(channel).isEmpty();
	}

}