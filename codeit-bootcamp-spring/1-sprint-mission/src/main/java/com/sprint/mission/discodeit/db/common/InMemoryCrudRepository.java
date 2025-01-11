package com.sprint.mission.discodeit.db.common;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
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
    public final T save(final T entity) {
        var id = Objects.requireNonNull(entity.getId());
        store.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(final ID id) {
        var findEntity = store.get(id);
        return Optional.ofNullable(findEntity);
    }

    @Override
    public List<T> findAll() {
        if (store.isEmpty()) {
            return Collections.emptyList();
        }
        var existEntities = store.values()
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return Collections.unmodifiableList(existEntities);
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
    public boolean isExistsById(final ID id) {
        var isExist = store.containsKey(id);
        return isExist;
    }
}
