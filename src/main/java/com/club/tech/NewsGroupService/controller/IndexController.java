package com.club.tech.NewsGroupService.controller;

import com.club.tech.NewsGroupService.model.UserInfo;
import com.club.tech.NewsGroupService.model.response.Response;
import com.club.tech.NewsGroupService.model.response.SuccessJwtResponse;
import com.club.tech.NewsGroupService.service.JwtHelperService;
import com.club.tech.NewsGroupService.service.NewsDataTransformerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class IndexController {
  private static final Logger log = LoggerFactory.getLogger(IndexController.class);
  private final NewsDataTransformerImpl newsDataTransformerImpl;
  private final JwtHelperService jwtHelperService;
  private final AuthenticationManager authenticationManager;

  public IndexController(
      NewsDataTransformerImpl newsDataTransformerImpl,
      JwtHelperService jwtHelperService,
      AuthenticationManager authenticationManager) {
    this.newsDataTransformerImpl = newsDataTransformerImpl;
    this.jwtHelperService = jwtHelperService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping(value = "/v1/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SuccessJwtResponse> authenticateUser(@RequestBody UserInfo userInfo) {

    var authObj =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userInfo.username(), userInfo.password()));

    if (authObj.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.OK.value())
          .body(
              new SuccessJwtResponse(
                  HttpStatus.OK.value(), jwtHelperService.getToken(userInfo.username())));
    } else {
      throw new RuntimeException("Invalid User req");
    }
  }

  @GetMapping(value = "/v1/newsArticles/query",consumes = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<Response> getNewsArticlesByIntervals(
      @RequestParam String searchKey,
      @RequestParam(required = false) Integer digit,
      @RequestParam(required = false) String interval,
      @RequestHeader() HttpHeaders header) {

    log.info("@@ getNewsArticlesByInterval API WAS INVOKED");
    return newsDataTransformerImpl.groupArticlesByInterval(
        Optional.ofNullable(searchKey), Optional.ofNullable(digit), Optional.ofNullable(interval));
  }
}
