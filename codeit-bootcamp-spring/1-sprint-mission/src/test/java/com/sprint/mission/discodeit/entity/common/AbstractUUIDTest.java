package com.sprint.mission.discodeit.entity.common;

import static com.sprint.mission.discodeit.entity.common.Status.CREATED;
import static com.sprint.mission.discodeit.entity.common.Status.MODIFY;
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
                () -> assertThat(entity.getCreateAt()).as("createdAt should not be null").isNotNull(),
                () -> assertThat(entity.getStatus()).isEqualTo(CREATED)
        );
    }

    @Test
    @DisplayName("엔티티 내 데이터가 수정 시 updateAt and status 필드 수정 여부 테스트")
    void givenWhenSomeDateModifyThenFieldUpdateAtAndStatusIsChangedTest() {
        // given, when
        var updatedTime = entity.update();

        //then
        assertAll(
                () -> assertThat(entity.getUpdateAt()).isEqualTo(updatedTime),
                () -> assertThat(entity.getStatus()).isEqualTo(MODIFY)
        );
    }
}
