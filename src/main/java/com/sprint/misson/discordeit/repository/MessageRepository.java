package com.sprint.misson.discordeit.repository;

import com.sprint.misson.discordeit.entity.Message;

import java.util.List;

public interface MessageRepository {

    Message save(Message message);

    boolean delete(Message message);

    Message findById(String id);

    List<Message> findAll();
}
