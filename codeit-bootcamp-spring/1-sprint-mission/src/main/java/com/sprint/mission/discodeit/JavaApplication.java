package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.file.user.FileUserRepository;
import java.io.IOException;

public class JavaApplication {
    public static void main(String[] args) throws IOException {
        try (FileUserRepository repo = (FileUserRepository) FileUserRepository.getInstance()) {
            System.out.println("======================");
            repo.findAll().forEach(System.out::println);
            System.out.println("======================");
        }
    }
}
