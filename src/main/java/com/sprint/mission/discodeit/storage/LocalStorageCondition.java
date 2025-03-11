package com.sprint.mission.discodeit.storage;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LocalStorageCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    String storageType = context.getEnvironment().getProperty("discodeit.storage.type");
    return "local".equals(storageType);
  }
}
