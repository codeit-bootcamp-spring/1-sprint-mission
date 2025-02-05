package com.sprint.misson.discordeit.repository.jcf;

import com.sprint.misson.discordeit.entity.status.ReadStatus;
import com.sprint.misson.discordeit.repository.ReadStatusRepository;

import java.util.HashMap;
import java.util.List;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final HashMap<String, ReadStatus> data;

    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findById(String id) {
        return data.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean delete(String id) {
        return data.remove(id) != null;
    }
}
