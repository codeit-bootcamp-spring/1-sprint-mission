package some_path._1sprintmission.discodeit.service.jcf;


import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.jcf.JCFUserRepository;
import some_path._1sprintmission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final JCFUserRepository userRepository = new JCFUserRepository();
    private final Random random = new Random();

    @Override
    public void createUser(User user) {
        int discriminator = makeDiscriminator(); // Generate unique discriminator
        user.setDiscriminator(discriminator);
        user.setVerified(false);
        userRepository.save(user); // Save using repository
    }

    @Override
    public Optional<User> getUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(System.out::println); // Print user details
        return user;
    }

    @Override
    public List<User> getUserAll() {
        return userRepository.findAll(); // Use repository to fetch all users
    }

    @Override
    public void updateUser(UUID id, User updatedUser) {
        userRepository.update(id, updatedUser); // Use repository to update user
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(id); // Use repository to delete user
    }

    @Override
    public int makeDiscriminator() {
        int sureNumber;
        while (true) {
            int notSureNumber = random.nextInt(10000);
            if (!userRepository.isDiscriminatorDuplicate(notSureNumber)) {
                sureNumber = notSureNumber;
                break;
            }
        }
        return sureNumber;
    }
}