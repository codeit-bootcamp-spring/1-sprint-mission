package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 사용자명을_이용해_사용자를_조회할_수_있다() {
		// given
		String username = "user1";

		// when
		Optional<User> user = userRepository.findByUsername(username);

		// then
		assertThat(user).isPresent();
		assertThat(user.get().getUsername()).isEqualTo(username);
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 존재하지_않는_사용자명을_조회하면_빈_Optional을_반환한다() {
		// given
		String username = "nonexistent";

		// when
		Optional<User> user = userRepository.findByUsername(username);

		// then
		assertThat(user).isEmpty();
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 이메일이_존재하면_true를_반환한다() {
		// given
		String email = "user1@example.com";

		// when
		boolean exists = userRepository.existsByEmail(email);

		// then
		assertThat(exists).isTrue();
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 존재하지_않는_이메일이면_false를_반환한다() {
		// given
		String email = "notfound@example.com";

		// when
		boolean exists = userRepository.existsByEmail(email);

		// then
		assertThat(exists).isFalse();
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 모든_사용자를_프로필과_상태와_함께_조회할_수_있다() {
		// when
		List<User> users = userRepository.findAllWithProfileAndStatus();

		// then
		assertThat(users).isNotEmpty();
		assertThat(users.get(0).getStatus()).isNotNull();
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 사용자를_삭제하면_데이터베이스에서_사라져야_한다() {
		// given
		UUID userId = UUID.fromString("aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

		// when
		userRepository.deleteById(userId);
		entityManager.flush();

		// then
		Optional<User> deletedUser = userRepository.findById(userId);
		assertThat(deletedUser).isEmpty();
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 중복된_이메일로_사용자를_추가하면_예외가_발생한다() {
		// given
		User user = new User("newUser", "user1@example.com", "password123", null);

		// when & then
		assertThatThrownBy(() -> userRepository.save(user))
			.isInstanceOf(UserAlreadyExistsException.class);
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 중복된_사용자명으로_사용자를_추가하면_예외가_발생한다() {
		// given
		User user = new User("user1", "newemail@example.com", "password123", null);

		// when & then
		assertThatThrownBy(() -> userRepository.save(user))
			.isInstanceOf(UserAlreadyExistsException.class);
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 존재하지_않는_사용자를_삭제하면_예외가_발생한다() {
		// given
		UUID nonExistentUserId = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

		// when & then
		assertThatThrownBy(() -> userRepository.deleteById(nonExistentUserId))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@Sql(scripts = {"/user/users.sql"})
	void 상태가_없는_사용자도_조회될_수_있다() {
		// given
		User userWithoutStatus = new User("noStatusUser", "nostatus@example.com", "password123", null);
		userRepository.save(userWithoutStatus);

		// when
		List<User> users = userRepository.findAllWithProfileAndStatus();

		// then
		assertThat(users).anyMatch(user -> user.getUsername().equals("noStatusUser"));
	}
}
