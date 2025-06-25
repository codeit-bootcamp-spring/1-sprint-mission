package com.sprint.mission.discodeit.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsyncTaskFailureRepository extends
    JpaRepository<com.sprint.mission.discodeit.entity.AsyncTaskFailure, UUID> {

}
