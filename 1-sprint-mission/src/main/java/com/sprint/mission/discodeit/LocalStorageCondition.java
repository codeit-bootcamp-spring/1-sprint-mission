package com.sprint.mission.discodeit;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LocalStorageCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment env = context.getEnvironment();
    String storageType = env.getProperty("discodeit.storage.type");
    return "local".equalsIgnoreCase(storageType);
  }
}
