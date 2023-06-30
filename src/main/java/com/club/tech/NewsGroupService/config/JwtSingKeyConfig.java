package com.club.tech.NewsGroupService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtSingKeyConfig(String signKey) {}
