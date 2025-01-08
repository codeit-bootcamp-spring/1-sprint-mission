package com.sprint.mission.discodeit.entity.common;

import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static com.sprint.mission.discodeit.entity.common.Status.REGISTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AbstractUUIDTest {

    private AbstractUUIDEntity entity;


    @BeforeEach
    void init() {
        entity = new TestUUIDEntity();
    }

    @Test
    @DisplayName("공통 엔티티 추상 클래스 생성 시 id, createdAt, status 필드 초기화 시 Not Null test")
    void createAbstractUUIDThenInitializeFieldIdAndCreatedAtAndStatusIsNotNullTest() {
        assertAll(
                () -> assertThat(entity.getId()).as("Id should not be null").isNotNull(),
                () -> assertThat(entity.getCreateAt()).as("createAt should not be null").isNotNull(),
                () -> assertThat(entity.getStatus()).isEqualTo(REGISTERED),
                () -> assertThat(entity.getUpdateAt()).as("At Initialized updateAt time is must be Empty").isEmpty()
        );
    }

    @Test
    @DisplayName("엔티티 내 데이터가 수정 시 updateAt and status 필드 수정 여부 테스트")
    void givenWhenSomeDateModifyThenFieldUpdateAtAndStatusIsChangedTest() {
        // given
        assertThat(entity.getUpdateAt()).isEmpty();
        // when
        entity.updateStatusAndUpdateAt();
        //then
        assertAll(
                () -> assertThat(entity.getUpdateAt()).as("invoke update() then updateAt must be present").isPresent(),
                () -> assertThat(entity.getStatus()).isEqualTo(MODIFIED)
        );
    }

}
