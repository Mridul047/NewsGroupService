package com.club.tech.NewsGroupService.model.response;

public record FaultResponse(int statusCode, String message) implements Response {}
