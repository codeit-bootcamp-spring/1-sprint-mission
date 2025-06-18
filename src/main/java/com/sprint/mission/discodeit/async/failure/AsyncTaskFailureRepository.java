package com.sprint.mission.discodeit.async.failure;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsyncTaskFailureRepository extends JpaRepository<AsyncTaskFailure, UUID> {

  List<AsyncTaskFailure> findByRequestId(String requestId);
}
