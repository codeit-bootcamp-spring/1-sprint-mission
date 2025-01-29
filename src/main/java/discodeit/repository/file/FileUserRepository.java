package discodeit.repository.file;

import discodeit.enity.User;
import discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    @Override
    public void save(User user) {
        // ser 파일에 Map으로 저장
        Map<UUID, User> users = this.findAll();
        if (users == null) {
            users = new HashMap<>();
        }
        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            // 기존 Map을 읽어와 새로운 객체를 추가한 후 덮어씌움
            users.put(user.getId(), user);
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> users = this.findAll();
        if (users == null || !users.containsKey(userId)) {
            throw new NoSuchElementException("User ID: " + userId + " not found");
        }
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        users.remove(userId);
        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public User findById(UUID userId) {
        Map<UUID, User> users = findAll();
        Optional<User> finding_user = users.values().stream()
                .filter(user -> user.getId().equals(userId))
                .findAny();
        return finding_user
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " not found"));
    }

    public Map<UUID, User> findAll() {
        Map<UUID, User> users = new HashMap<>();
        try (FileInputStream fis = new FileInputStream("user.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            users = (Map<UUID, User>) ois.readObject();
        } catch (EOFException e) {
            return users;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return users;
    }
}
