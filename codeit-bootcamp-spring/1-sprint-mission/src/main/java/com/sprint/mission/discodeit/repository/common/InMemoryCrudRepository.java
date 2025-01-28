package com.sprint.mission.discodeit.repository.common;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class InMemoryCrudRepository<T extends AbstractUUIDEntity, ID extends UUID>
        implements CrudRepository<T, ID> {

    protected final Map<UUID, T> store = new HashMap<>();

    @Override
    public final T save(T entity) {
        var id = Objects.requireNonNull(entity.getId());
        store.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        var findEntity = store.get(id);
        return Optional.ofNullable(findEntity);
    }

    @Override
    public List<T> findAll() {
        if (store.isEmpty()) {
            return Collections.emptyList();
        }

        return List.copyOf(store.values());
    }

    @Override
    public int count() {
        return store.size();
    }

    @Override
    public void deleteById(ID id) {
        store.remove(id);
    }

    @Override
    public boolean isExistsById(ID id) {
        var isExist = store.containsKey(id);
        return isExist;
    }
}
