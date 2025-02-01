package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JCFUserRepository implements UserRepository {
    private final List<User> data;
    public JCFUserRepository() {
        data = new ArrayList<>();
    }

    // 저장
    public boolean saveUser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("user is null");
            }
            if (data.contains(user)) {
                deleteUser(user);
            }
            data.add(user);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // 조회
    public User loadUser(User user) {
        return data.stream().filter(d -> d.getId().equals(user.getId()))
                .findFirst().orElse(null);
    }
    public List<User> loadAllUser() {
        return Collections.unmodifiableList(data);
    }

    // 삭제
    public boolean deleteUser(User user) {
        try {
            if (!data.contains(user)) {
                throw new IllegalArgumentException("User not found");
            }
            if (data.remove(user)) {
                return true;
            }
            throw new RuntimeException("Failed to remove user");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
