package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.storage.s3.AwsS3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(AwsS3Properties.class)
public class DiscodeitApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
	}
}
