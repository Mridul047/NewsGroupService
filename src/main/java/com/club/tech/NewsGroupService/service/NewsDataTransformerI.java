package com.club.tech.NewsGroupService.service;

import com.club.tech.NewsGroupService.model.response.Response;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

public interface NewsDataTransformerI {

  ResponseEntity<Response> groupArticlesByInterval(
      Optional<String> searchKey, Optional<Integer> n, Optional<String> interval);
}
