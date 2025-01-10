package com.sprint.mission.discodeit.db.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.testdummy.TestDummyInMemoryCurdRepository;
import com.sprint.mission.discodeit.testdummy.TestUUIDEntity;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("레포지토리 추상 클래스 테스트")
class InMemoryCrudRepositoryTest {

    private static TestDummyInMemoryCurdRepository repository;

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

        @Test
        @DisplayName("새로운 객체를 생성하여 저장했을 때 동일한 객체가 리턴")
        void givenNewEntityWhenSaveEntityThenReturnNewEntity() {
            // given
            TestUUIDEntity newEntity = new TestUUIDEntity();
            // when
            var savedEntity = repository.save(newEntity);
            // then
            assertThat(savedEntity).isEqualTo(newEntity);
        }

        @Test
        @DisplayName("레포지토리에 저장 시 총 저장되어 있는 개수 증가")
        void givenSaveNewEntityWhenCountRepositoryThenSizeIncrease() {
            // given
            var oldCount = repository.count();
            repository.save(new TestUUIDEntity());
            // when
            var newCount = repository.count();
            // then
            assertThat(newCount).isEqualTo(oldCount + 1);
        }

        @Test
        @DisplayName("저장한 객체와 동일한 객체의 id로 가져온 객체 비교")
        void givenSavedEntityWhenFindByIdThenReturnEqualIsTrue() {
            // given
            var createdEntity = new TestUUIDEntity();
            repository.save(createdEntity);
            // when
            var findEntity = repository.findById(createdEntity.getId()).orElse(null);
            //then
            assertThat(findEntity).isEqualTo(createdEntity);
        }

        @Test
        @DisplayName("새로운 객체를 저장 후 삭제 시 정상 작동")
        void givenNewEntitySaveWhenDeleteEntityThenStoreSizeNoChange() {
            // given
            int storeSize = repository.count();
            var newEntity = new TestUUIDEntity();
            repository.save(newEntity);
            // when
            repository.deleteById(newEntity.getId());
            // then
            assertThat(repository.count()).isEqualTo(storeSize);
        }
    }

    @Nested
    @DisplayName("저장된 객체")
    class whenFindEntity {
        private static final int REPOSITORY_SIZE = 10;
        private static TestUUIDEntity entity;

        @BeforeAll
        static void setUp() {
            repository = new TestDummyInMemoryCurdRepository();
            entity = new TestUUIDEntity();
            repository.save(entity);
            for (int i = 0; i < REPOSITORY_SIZE; i++) {
                repository.save(new TestUUIDEntity());
            }
        }

        @Test
        @DisplayName("확실히 저장되어 있는 객체를 id로 레포지터리에서 찾을 때 true 리턴")
        void givenEnsureEntityIdWhenFindByIdThenReturnTrue() {
            // given
            var existId = entity.getId();
            // when
            var result = repository.existsById(existId);
            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 id로 레포지터리에서 찾을 때 false 리턴")
        void givenGenerateUUIDWhenFindByIdThenReturnFalse() {
            // given
            var id = UUID.randomUUID();
            // when
            var result = repository.existsById(id);
            //then
            assertThat(result).isFalse();
        }

        // 모든 저장된 객체를 가져오기
        @Test
        @DisplayName("저장된 모든 객체를 리스트로 조회시 레포지터리 count 메서드의 결과 비교")
        void whenFindAllThenReturnAll() {
            // given
            int repositorySize = repository.count();
            // when
            var allEntity = repository.findAll();
            // then
            assertAll(
                    () -> assertThat(allEntity).isNotEmpty(),
                    () -> assertThat(allEntity.size()).isEqualTo(repositorySize)
            );
        }
    }
}