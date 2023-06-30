package com.club.tech.NewsGroupService.config;

import com.club.tech.NewsGroupService.model.UserSecurityInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "news")
public record NewsApiConfig(String apiKey, String url, List<UserSecurityInfo> users) {}
