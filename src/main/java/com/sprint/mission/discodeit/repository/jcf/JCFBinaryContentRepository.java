package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jsf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        this.data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findByUser(User user) {
        return this.data.values().stream()
                .filter(content -> content.getUser().equals(user))
                .findFirst();
    }

    @Override
    public void deleteByUser(User user) {
        this.data.values().removeIf(content -> content.getUser().equals(user));
    }

    @Override
    public List<BinaryContent> findByMessage(Message message) {
        return this.data.values().stream()
                .filter(content -> content.getMessage().equals(message))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteByMessageId(Message message) {
        this.data.values().removeIf(content -> content.getMessage().equals(message));
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this.data::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }
}
