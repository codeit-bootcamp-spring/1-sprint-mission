package com.example.mission.discodeit.repository;


import com.example.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}

