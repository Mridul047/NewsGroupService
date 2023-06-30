package com.club.tech.NewsGroupService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public UserDetailsService userDetailsService(
      PasswordEncoder encoder, NewsApiConfig newsApiConfig) {

    var authorizedUsersList = newsApiConfig.users();

    UserDetails adminUser =
        User.withUsername(authorizedUsersList.get(0).authorizedUser())
            .password(encoder.encode(authorizedUsersList.get(0).password()))
            .roles(authorizedUsersList.get(0).roles())
            .build();

    UserDetails generalUser =
        User.withUsername(authorizedUsersList.get(1).authorizedUser())
            .password(encoder.encode(encoder.encode(authorizedUsersList.get(1).password())))
            .roles(authorizedUsersList.get(1).roles())
            .build();
    return new InMemoryUserDetailsManager(adminUser, generalUser);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
