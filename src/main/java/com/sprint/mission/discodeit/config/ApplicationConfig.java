package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;
import lombok.Getter;

@Getter
public class ApplicationConfig {

  private ServiceType channelServiceType;
  private StorageType channelStorageType;

  private ServiceType userServiceType;
  private StorageType userStorageType;

  private ServiceType messageServiceType;
  private StorageType messageStorageType;

  private ApplicationConfig(ApplicationConfigBuilder builder) {
    this.channelServiceType = builder.channelServiceType;
    this.channelStorageType = builder.channelStorageType;
    this.userServiceType = builder.userServiceType;
    this.userStorageType = builder.userStorageType;
    this.messageServiceType = builder.messageServiceType;
    this.messageStorageType = builder.messageStorageType;
  }


  public static class ApplicationConfigBuilder {
    private ServiceType channelServiceType;
    private StorageType channelStorageType;

    private ServiceType userServiceType;
    private StorageType userStorageType;

    private ServiceType messageServiceType;
    private StorageType messageStorageType;

    public ApplicationConfigBuilder channelServiceType(ServiceType type) {
      this.channelServiceType = type;
      return this;
    }

    public ApplicationConfigBuilder channelStorageType(StorageType type) {
      this.channelStorageType = type;
      return this;
    }

    public ApplicationConfigBuilder userServiceType(ServiceType type) {
      this.userServiceType = type;
      return this;
    }

    public ApplicationConfigBuilder userStorageType(StorageType type) {
      this.userStorageType = type;
      return this;
    }

    public ApplicationConfigBuilder messageServiceType(ServiceType type) {
      this.messageServiceType = type;
      return this;
    }

    public ApplicationConfigBuilder messageStorageType(StorageType type) {
      this.messageStorageType = type;
      return this;
    }

    public ApplicationConfig build() {
      return new ApplicationConfig(this);
    }
  }
}
