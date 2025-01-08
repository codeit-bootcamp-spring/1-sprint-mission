package sprint.mission.discodeit.validation;

import sprint.mission.discodeit.exception.InvalidException;

public interface Validator<T> {
    void validate(T entity) throws InvalidException;
}
