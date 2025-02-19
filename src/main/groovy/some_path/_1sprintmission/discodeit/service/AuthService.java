package some_path._1sprintmission.discodeit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import some_path._1sprintmission.discodeit.dto.UserDTO;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.UserRepository;
import some_path._1sprintmission.discodeit.repository.UserStatusRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public UserDTO login(String username, String password) {
        User user = userRepository.findByUserName(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        boolean isOnline = userStatusRepository.findById(user.getId()).isPresent();

        return UserDTO.from(user, isOnline);
    }
}
