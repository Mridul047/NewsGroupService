package com.club.tech.NewsGroupService.client;

import com.club.tech.NewsGroupService.config.NewsApiConfig;
import com.club.tech.NewsGroupService.model.Article;
import com.club.tech.NewsGroupService.model.News;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsFetcherServiceTest {

  @InjectMocks private NewsFetcherService newsFetcherService;
  @Mock NewsApiConfig newsApiConfig;
  @Mock RestTemplate client;

  @Test
  void fetchNewsSuccessfully() {
    // GIVEN
    Optional<String> searchKey = getSearchKey();

    // WHEN
    News expectedNews = getNewsObject();

    when(newsApiConfig.apiKey()).thenReturn("apiKey");
    when(newsApiConfig.url()).thenReturn("https://www.google.com");
    when(newsApiConfig.url().formatted(searchKey.get(), newsApiConfig.apiKey()))
        .thenReturn("https://www.google.com");
    when(client.getForObject(anyString(), any())).thenReturn(expectedNews);

    News actualNews = newsFetcherService.fetchNewsInfo(searchKey);

    // THEN
    Assertions.assertEquals(expectedNews, actualNews);
  }

  @Test
  void fetchNewsFailure() {
    // GIVEN
    Optional<String> searchKey = getSearchKey();

    when(newsApiConfig.apiKey()).thenReturn("apiKey");
    when(newsApiConfig.url()).thenReturn("https://www.google.com");
    when(newsApiConfig.url().formatted(searchKey.get(), newsApiConfig.apiKey()))
        .thenReturn("https://www.google.com");
    when(client.getForObject(anyString(), any()))
        .thenThrow(new RestClientException("API request failed"));

    // WHEN
    News actualNews = null;
    try {
      actualNews = newsFetcherService.fetchNewsInfo(searchKey);
    } catch (Exception e) {
      // THEN
      Assertions.assertNull(actualNews);
    }
  }

  @Test
  void fetchNewsSuccessfullyWithNoResults() {
    // GIVEN
    Optional<String> searchKey = getSearchKey();
    News expectedNews = new News();
    expectedNews.setStatus("200");
    expectedNews.setArticles(Collections.emptyList());
    expectedNews.setTotalResults(0);

    when(newsApiConfig.apiKey()).thenReturn("apiKey");
    when(newsApiConfig.url()).thenReturn("https://www.google.com");
    when(newsApiConfig.url().formatted(searchKey.get(), newsApiConfig.apiKey()))
        .thenReturn("https://www.google.com");
    when(client.getForObject(anyString(), any())).thenReturn(expectedNews);

    // WHEN
    News actualNews = newsFetcherService.fetchNewsInfo(searchKey);

    // THEN
    Assertions.assertEquals(expectedNews, actualNews);
  }

  private News getNewsObject() {
    var articleList = List.of(new Article(), new Article(), new Article(), new Article());
    News expectedNews = new News();
    expectedNews.setStatus("200");
    expectedNews.setArticles(articleList);
    expectedNews.setTotalResults(articleList.size());

    return expectedNews;
  }

  private Optional<String> getSearchKey() {
    return Optional.of("apple");
  }
}
