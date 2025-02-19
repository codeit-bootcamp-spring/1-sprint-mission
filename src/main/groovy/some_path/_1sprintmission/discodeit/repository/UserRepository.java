package some_path._1sprintmission.discodeit.repository;


import org.codehaus.groovy.vmplugin.v8.PluginDefaultGroovyMethods;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.validation.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    boolean existsById(UUID id);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    void deleteById(UUID id);

    boolean isDiscriminatorDuplicate(String username, int discriminator);

    Optional<User> findByUserName(String userName);
}