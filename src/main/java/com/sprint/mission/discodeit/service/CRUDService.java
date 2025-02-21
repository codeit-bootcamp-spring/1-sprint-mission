package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface CRUDService<Request, Response> {
    Response create(Request entity);
    Response readOne(UUID id);
    List<Response> readAll();
    Response update(UUID id, Request entity);
    boolean delete(UUID id);
}
