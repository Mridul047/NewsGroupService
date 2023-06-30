package com.club.tech.NewsGroupService.client;

import com.club.tech.NewsGroupService.config.NewsApiConfig;
import com.club.tech.NewsGroupService.model.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class NewsFetcherService {
  private static final Logger log = LoggerFactory.getLogger(NewsFetcherService.class);
  private final NewsApiConfig newsApiConfig;
  private final RestTemplate client;

  public NewsFetcherService(RestTemplate restTemplate, NewsApiConfig newsApiConfig) {
    this.client = restTemplate;
    this.newsApiConfig = newsApiConfig;
  }

  @Retryable
  public News fetchNewsInfo(Optional<String> searchKey) {
    News news = null;
    String news_url = newsApiConfig.url().formatted(searchKey.get(), newsApiConfig.apiKey());

    log.info("@@ CONSUMING NEWS API AT URL : %s".formatted(news_url));

    news = client.getForObject(news_url, News.class);
    return news;
  }
}
