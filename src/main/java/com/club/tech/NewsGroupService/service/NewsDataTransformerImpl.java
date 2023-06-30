package com.club.tech.NewsGroupService.service;

import com.club.tech.NewsGroupService.client.NewsFetcherService;
import com.club.tech.NewsGroupService.exception.customexception.NoArticlesFoundException;
import com.club.tech.NewsGroupService.model.Article;
import com.club.tech.NewsGroupService.model.News;
import com.club.tech.NewsGroupService.model.response.Response;
import com.club.tech.NewsGroupService.model.response.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class NewsDataTransformerImpl implements NewsDataTransformerI {

  private static final Logger log = LoggerFactory.getLogger(NewsDataTransformerImpl.class);
  private final NewsFetcherService newsFetcherService;
  private String currentDateTime = "";

  public NewsDataTransformerImpl(NewsFetcherService newsFetcherService) {
    this.newsFetcherService = newsFetcherService;
  }

  @Override
  public ResponseEntity<Response> groupArticlesByInterval(
      Optional<String> searchKey, Optional<Integer> n, Optional<String> interval)
      throws NoArticlesFoundException {

    List<Article> filteredArticleList = new LinkedList<>();
    int num = 12;
    String unit = "hours";
    currentDateTime =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

    News news = newsFetcherService.fetchNewsInfo(searchKey);

    if (n.isPresent() && interval.isPresent()) {
      num = n.get();
      unit = interval.get();
    }
    filteredArticleList = filterArticles(news, num, unit);

    if (filteredArticleList.size() == 0) {
      throw new NoArticlesFoundException(
          "No articles available for supplied interval. Try with different value !");
    } else {
      return ResponseEntity.ok(
          new SuccessResponse(
              HttpStatus.OK.value(),
              "Fetched articles based on supplied interval!",
              "%d %s".formatted(num, unit),
              filteredArticleList.size(),
              groupByPublishedDate(filteredArticleList)));
    }
  }

  private Map<String, List<Article>> groupByPublishedDate(List<Article> articleList) {

    return articleList.stream().collect(groupingBy(Article::getPublishedAt));
  }

  private List<Article> filterArticles(News news, int n, String interval) {
    List<Article> filteredArticles =
        news.getArticles().stream()
            .filter(
                article ->
                    compareDateTime(
                            LocalDateTime.parse(article.getPublishedAt().replace("Z", "")),
                            calculateStartDateTime(
                                LocalDateTime.parse(currentDateTime), n, interval))
                        > 0)
            .collect(Collectors.toList());

    log.info(
        "%s%s"
            .formatted(
                "@@ FILTERED LAST %d %s ARTICLES: ".formatted(n, interval), filteredArticles));
    return filteredArticles;
  }

  private int compareDateTime(LocalDateTime publishedDateTime, LocalDateTime startDateTime) {
    log.debug(
        "@@ COMPARING publishedDateTime %s WITH startDateTime %s"
            .formatted(publishedDateTime.toString(), startDateTime.toString()));
    return publishedDateTime.compareTo(startDateTime);
  }

  private LocalDateTime calculateStartDateTime(
      LocalDateTime currentDateTime, int n, String interval) {

    return currentDateTime.minus(n, getChronoUnit(interval));
  }

  private ChronoUnit getChronoUnit(String interval) {
    return switch (interval) {
      case "minutes" -> ChronoUnit.MINUTES;
      case "hours" -> ChronoUnit.HOURS;
      case "days" -> ChronoUnit.DAYS;
      case "weeks" -> ChronoUnit.WEEKS;
      case "months" -> ChronoUnit.MONTHS;
      case "years" -> ChronoUnit.YEARS;
      default -> throw new IllegalArgumentException("Argument supplied is not supported !");
    };
  }
}
