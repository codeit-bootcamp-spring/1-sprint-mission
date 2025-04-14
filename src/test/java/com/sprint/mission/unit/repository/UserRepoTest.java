package com.sprint.mission.unit.repository;


import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql")
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager tem;

    @DisplayName("User FindById 테스트")
    @Test
    void findByIdTest() {
        User user = new User("userA", "password123", "icb1112@naver.com", null);
        User persistAndFlush = tem.persistAndFlush(user);
        tem.clear();

        User foundUser = userRepository.findById(persistAndFlush.getId()).orElseGet(null);

        assertThat(foundUser).isNotNull().isEqualTo(user);
        assertThat(foundUser.getId()).isEqualTo(user.getId());
    }

    @DisplayName("username으로 조회")
    @Test
    void findByUsername() {
        String name = "찾는 이름";
        User user = new User(name, "pwd1234", "icc1123@naver.com", null);
        tem.persistAndFlush(user);
        tem.clear();

        User foundUser = userRepository.findByUsername(name).orElse(null);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
        assertThat(foundUser.getUsername()).isEqualTo(name);
        //assertThat(foundUser.getStatus()).isNotInstanceOf(Hibernate.class)
    }

    @DisplayName("userList EntityGraph의 N+1 해결 확인")
    @Test
    void userListEntityGraphTest(){

    }


    //public class User extends BaseUpdatableEntity{
    //
    //    private String username;
    //    private String email;
    //    private String password;
    //
    //    //변경가능하니
    //    @OneToOne(fetch = LAZY)
    //    @JoinColumn(name = "profile_id")
    //    @OnDelete(action = OnDeleteAction.SET_NULL) // profile이 삭제되면 user의 profile은 null로 변경
    //    private BinaryContent profile;
    //
    //    @OneToOne(mappedBy = "user", cascade = REMOVE)
    //    private UserStatus status;
    //
    //    // REMOVE => user가 삭제되면 readStatus도 삭제됨
    //    @OneToMany(mappedBy = "user", cascade = REMOVE, orphanRemoval = true)
    //    private List<ReadStatus> readStatus = new ArrayList<>();
}


