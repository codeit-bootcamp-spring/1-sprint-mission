package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import com.sprint.mission.discodeit.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@EnableJpaAuditing
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	public void 유저의_정보가_주어졌을_때_저장_메서드를_호출하여_저장소에_유저가_저장된다() throws Exception {
		//given
		User user = new User("유저1", "email@email.com", "1234", null);

		//when
		User savedUser = userRepository.save(user);
		em.flush();
		em.clear();

		//then
		assertThat(savedUser.getUsername()).isEqualTo("유저1");
		assertThat(savedUser.getEmail()).isEqualTo("email@email.com");
		assertThat(savedUser.getProfile()).isNull();
	}

	@Test
	public void 이미_존재하는_이메일로_인해_저장_메서드를_호출하여_저장소에_유저가_저장되지_않는다() throws Exception {
		//given
		User user1 = new User("유저1", "email@email.com", "1234", null);
		User user2 = new User("유저2", "email@email.com", "5678", null);
		em.persistAndFlush(user1);

		//when

		//then
		assertThatThrownBy(() -> {
			userRepository.save(user2);
			em.flush();
		}).isInstanceOf(ConstraintViolationException.class); // org.hibernate.exception.ConstraintViolationException
	}
}