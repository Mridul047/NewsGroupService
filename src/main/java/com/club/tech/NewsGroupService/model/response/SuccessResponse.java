package com.club.tech.NewsGroupService.model.response;

import com.club.tech.NewsGroupService.model.Article;
import java.util.List;
import java.util.Map;

public record SuccessResponse(
    int statusCode,
    String message,
    String intervalGroup,
    int totalArticles,
    Map<String, List<Article>> articleMap)
    implements Response {}
