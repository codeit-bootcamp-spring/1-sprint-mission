package some_path._1sprintmission.discodeit.repository.file;

import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.UserRepository;
import some_path._1sprintmission.discodeit.entiry.enums.UserType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {

    private final String filename;

    public FileUserRepository(String filename) {
        this.filename = filename;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = parseUser(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void saveAll(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            for (User user : users) {
                String userString = userToString(user);
                bw.write(userString);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User parseUser(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            System.err.println("Invalid user data: " + line);
            return null;
        }

        String username = parts[0].trim();
        String email = parts[1].trim();
        String phone = parts[2].trim();
        UserType userType = UserType.valueOf(parts[3].trim());
        String introduce = parts[4].trim();

        User user = new User(username, email, phone);
        user.setUserType(userType);
        user.setIntroduce(introduce);
        user.setVerified(Boolean.FALSE);
        return user;
    }

    private String userToString(User user) {
        return user.getUsername() + ","
                + user.getUserEmail() + ","
                + user.getUserPhone() + ","
                + user.getUserType() + ","
                + user.getIntroduce();
    }
}
