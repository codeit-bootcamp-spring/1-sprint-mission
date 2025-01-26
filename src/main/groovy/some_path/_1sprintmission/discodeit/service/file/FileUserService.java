package some_path._1sprintmission.discodeit.service.file;

import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.UserRepository;

import java.util.List;

public class FileUserService {

    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> loadUserList() {
        return userRepository.findAll();
    }

    public void saveUserList(List<User> users) {
        userRepository.saveAll(users);
    }
}
