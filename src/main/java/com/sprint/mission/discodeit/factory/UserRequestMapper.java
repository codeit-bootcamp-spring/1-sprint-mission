package com.sprint.mission.discodeit.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper implements RequestMapper<UserCommonRequest> {
    @Override
    public UserCommonRequest stringToJson(String request, Class<?> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(request, UserCommonRequest.class);
    }
}
