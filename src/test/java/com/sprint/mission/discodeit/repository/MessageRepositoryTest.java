package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.sprint.mission.discodeit.entity.Message;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@Sql(scripts = {"/user/users.sql", "/channel/channels.sql", "/message/messages.sql"})
	void 특정_채널의_메시지를_조회할_수_있다() {
		// given
		UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Instant fixedCreatedAt = Instant.parse("2025-05-01T00:00:00Z"); // 고정된 시간 설정
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		// when
		Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(channelId, fixedCreatedAt, pageable);

		// then
		assertThat(messages).isNotEmpty();
		assertThat(messages.getContent().get(0).getChannel().getId()).isEqualTo(channelId);
	}

	@Test
	@Sql(scripts = {"/user/users.sql", "/channel/channels.sql", "/message/messages.sql"})
	void 특정_채널의_마지막_메시지_시간을_조회할_수_있다() {
		// given
		UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111111");

		// when
		Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(channelId);

		// then
		assertThat(lastMessageAt).isPresent();
	}

	@Test
	@Sql(scripts = {"/user/users.sql", "/channel/channels.sql", "/message/messages.sql"})
	void 특정_채널의_모든_메시지를_삭제할_수_있다() {
		// given
		UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111111");

		// when
		messageRepository.deleteAllByChannelId(channelId);
		entityManager.flush();

		// then
		List<Message> messages = messageRepository.findAll();
		assertThat(messages).noneMatch(m -> m.getChannel().getId().equals(channelId));
	}
}