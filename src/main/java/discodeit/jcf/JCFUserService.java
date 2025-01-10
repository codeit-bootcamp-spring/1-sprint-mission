package discodeit.jcf;

import discodeit.entity.User;
import discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public void create(User user) {
        data.put(user.getId(), user);
        System.out.println("User created: " + user.getUsername());
    }

    @Override
    public User read(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("User not found");
            return null;
        }
        System.out.println("User read: " + data.get(id).getUsername());
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String username) {
        User user = data.get(id);
        if(user != null) {
            System.out.print("User updated: " + user.getUsername());
            user.updateUpdatedAt();
            user.updateUsername(username);
            System.out.println(" -> " + user.getUsername());
        }
    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)) {
            System.out.println("User not found for delete");
        }
        data.remove(id);
        System.out.println("User removed");
    }
}
