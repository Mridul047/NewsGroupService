package com.club.tech.NewsGroupService;

import com.club.tech.NewsGroupService.config.JwtSingKeyConfig;
import com.club.tech.NewsGroupService.config.NewsApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableConfigurationProperties({NewsApiConfig.class, JwtSingKeyConfig.class})
@EnableRetry
public class NewsGroupServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsGroupServiceApplication.class, args);
  }
}
