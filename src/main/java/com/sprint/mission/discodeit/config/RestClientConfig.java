package com.sprint.mission.discodeit.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    var factory = new HttpComponentsClientHttpRequestFactory(httpClient);
    return RestClient.builder().requestFactory(factory).build();
  }
}
