package com.sprint.mission.discodeit.db.common;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.testdummy.TestDummyInMemoryCurdRepository;
import com.sprint.mission.discodeit.testdummy.TestUUIDEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("레포지토리 추상 클래스 테스트")
class InMemoryCrudRepositoryTest {

    private TestDummyInMemoryCurdRepository repository;

    @Test
    void initializeRepositoryTest() {
        assertThat(new TestDummyInMemoryCurdRepository()).isNotNull();
    }

    @Nested
    @DisplayName("데이터를 저장했을 때")
    class whenAddEntity {

        @BeforeEach
        void setUp() {
            repository = new TestDummyInMemoryCurdRepository();
        }

        // 저장하고 결과가 잘 나오는지
        @Test
        @DisplayName("새로운 객체를 생성하여 저장했을 때 동일한 객체가 리턴 테스트")
        void givenNewEntityWhenSaveEntityThenReturnNewEntity() {
            // given
            TestUUIDEntity newEntity = new TestUUIDEntity();
            // when
            var saveEntity = repository.save(newEntity);
            // then
            assertThat(saveEntity).isEqualTo(newEntity);
        }
        // 저장한 후 자료구조 사이즈가 증가 되었는지

        // 저장 후 제대로 저장이 되어있는지
    }
}