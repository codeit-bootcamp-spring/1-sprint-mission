package com.sprint.mission.discodeit.util.validation;

public interface Validator<T, U, V> {
    void validateCreate(U entity);

    T validateUpdate(T current, V update);
}
