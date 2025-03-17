package com.sprint.mission.discodeit.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  @Transactional
  public void applyOnDeleteCascade() {
    try {
      entityManager.createNativeQuery(
          "ALTER TABLE read_status " +
              "ADD CONSTRAINT user_id FOREIGN KEY (using_id) " +
              "REFERENCES users(id) ON DELETE CASCADE"
      ).executeUpdate();
    } catch (Exception e) {
      System.out.println("Foreign key already exists or error occurred: " + e.getMessage());
    }
  }
}