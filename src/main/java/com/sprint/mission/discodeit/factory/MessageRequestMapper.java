package com.sprint.mission.discodeit.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.message.create.MessageCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MessageRequestMapper implements RequestMapper<MessageCreateRequest> {

  @Override
  public MessageCreateRequest stringToJson(String request, Class<?> clazz)
      throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(request, MessageCreateRequest.class);
  }
}
