package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UsersDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Primary
@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<String, User> dataStore = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        dataStore.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(String id) {
        dataStore.remove(id);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    public List<UsersDTO> findAll() {
        return dataStore.values().stream()
                .map(this::convertToUsersDTO)
                .collect(Collectors.toList());
    }

    private UsersDTO convertToUsersDTO(User user) {
        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());

        if (user.getEmail() != null) {
            dto.setEmail(user.getEmail());
        }

        dto.setOnline(user.isOnline());

        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            dto.setProfileImage(Base64.getEncoder().encodeToString(user.getProfileImage()));
        }

        return dto;
    }
}
