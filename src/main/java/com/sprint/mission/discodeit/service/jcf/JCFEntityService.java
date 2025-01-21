package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.service.EntityService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// 싱글톤 패턴을 적용한 JCF 기반의 EntityService 구현체
public class JCFEntityService<T extends BaseEntity> implements EntityService<T> {
    private final Map<UUID, T> data = new ConcurrentHashMap<>(); // 데이터를 저장할 Map

    // 싱글톤 인스턴스를 엔티티별로 관리
    private static final Map<Class<?>, JCFEntityService<?>> instances = new HashMap<>();

    // private 생성자
    private JCFEntityService() {}

    // 싱글톤 인스턴스를 반환하는 메소드
    @SuppressWarnings("unchecked")
    public static synchronized <T extends BaseEntity> JCFEntityService<T> getInstance(Class<T> entityType) {
        return (JCFEntityService<T>) instances.computeIfAbsent(entityType, k -> new JCFEntityService<>());
    }

    @Override
    public T create(T entity) {
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<T> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public T update(UUID id, T entity) {
        if (data.containsKey(id)) {
            data.put(id, entity);
            return entity;
        }
        throw new NoSuchElementException("Entity not found for update");
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    // Stream API를 사용하여 조건에 맞는 객체를 필터링하여 반환
    public List<T> findByCondition(Predicate<T> predicate) {
        return data.values().stream().filter(predicate).collect(Collectors.toList());
    }
}