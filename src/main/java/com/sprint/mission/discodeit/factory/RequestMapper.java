package com.sprint.mission.discodeit.factory;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RequestMapper<T> {
    T stringToJson(String request, Class<?> clazz) throws JsonProcessingException;
}
