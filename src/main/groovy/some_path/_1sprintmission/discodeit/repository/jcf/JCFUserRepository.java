package some_path._1sprintmission.discodeit.repository.jcf;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.*;

public class JCFUserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    // Save a user
    public void save(User user) {
        data.put(user.getId(), user);
    }

    // Find a user by ID
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    // Find all users
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    // Update a user
    public void update(UUID id, User updatedUser) {
        data.put(id, updatedUser);
    }

    // Delete a user
    public void delete(UUID id) {
        data.remove(id);
    }

    // Check if a discriminator is duplicate
    public boolean isDiscriminatorDuplicate(int discriminator) {
        return data.values().stream()
                .anyMatch(user -> Objects.equals(user.getDiscriminator(), discriminator));
    }
}

