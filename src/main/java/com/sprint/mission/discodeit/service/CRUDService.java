package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface CRUDService<Req, Res> {
    Res create(Req entity);
    Res readOne(UUID id);
    List<Res> readAll();
    Res update(UUID id, Req entity);
    boolean delete(UUID id);
}
