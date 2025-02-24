package com.sprint.mission.discodeit.util.validation;

public interface Validator<T, U> {
    void validateCreate(U entity);

    T validateUpdate(T current, U update);
}
