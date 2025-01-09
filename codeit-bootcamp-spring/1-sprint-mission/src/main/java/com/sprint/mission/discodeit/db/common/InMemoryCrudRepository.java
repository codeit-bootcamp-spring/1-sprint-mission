package com.sprint.mission.discodeit.db.common;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class InMemoryCrudRepository<T extends AbstractUUIDEntity, ID extends UUID>
        implements CrudRepository<T, ID> {

    protected final Map<UUID, T> store = new HashMap<>();

    @Override
    public T save(final T entity) {
        var id = Objects.requireNonNull(entity.getId());
        var savedEntity = store.put(id, entity);
        return savedEntity;
    }

    @Override
    public Optional<T> findById(final ID id) {
        var findEntity = store.get(id);
        return Optional.ofNullable(findEntity);
    }

    @Override
    public Iterable<T> findAll() {
        if (store.isEmpty()) {
            return Collections.emptyList();
        }
        var existEntities = store.values()
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return existEntities;
    }

    @Override
    public int count() {
        return store.size();
    }

    @Override
    public void deleteById(final ID id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(final ID id) {
        var isExist = store.containsKey(id);
        return isExist;
    }
}
