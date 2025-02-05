package com.sprint.misson.discordeit.repository.jcf;

import com.sprint.misson.discordeit.entity.BinaryContent;
import com.sprint.misson.discordeit.repository.BinaryContentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<String, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(String id) {
        return data.get(id);
    }

    @Override
    public List<BinaryContent> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean delete(String id) {
        return data.remove(id) != null;
    }
}
