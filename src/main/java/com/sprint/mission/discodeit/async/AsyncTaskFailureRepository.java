package com.sprint.mission.discodeit.async;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsyncTaskFailureRepository extends JpaRepository<AsyncTaskFailure, UUID> {

}
