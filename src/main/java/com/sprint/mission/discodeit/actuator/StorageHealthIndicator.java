package com.sprint.mission.discodeit.actuator;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class StorageHealthIndicator implements HealthIndicator {

    @Value("${storage.path}")
    private String storagePath;

    @Override
    public Health health() {
        try {
            File storageDir = new File(storagePath);
            if (!storageDir.exists()) {
                return Health.down()
                    .withDetail("error", "Storage directory does not exist")
                    .withDetail("path", storagePath)
                    .build();
            }

            if (!storageDir.canWrite()) {
                return Health.down()
                    .withDetail("error", "Storage directory is not writable")
                    .withDetail("path", storagePath)
                    .build();
            }

            long totalSpace = storageDir.getTotalSpace();
            long freeSpace = storageDir.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            double usagePercentage = (double) usedSpace / totalSpace * 100;

            return Health.up()
                .withDetail("total_space_mb", totalSpace / (1024 * 1024))
                .withDetail("free_space_mb", freeSpace / (1024 * 1024))
                .withDetail("used_space_mb", usedSpace / (1024 * 1024))
                .withDetail("usage_percentage", String.format("%.2f%%", usagePercentage))
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .withDetail("path", storagePath)
                .build();
        }
    }
} 