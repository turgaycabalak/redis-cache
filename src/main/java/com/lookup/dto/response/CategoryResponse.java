package com.lookup.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CategoryResponse(
    Long id,
    String name,
    LocalDateTime createdDate
) {
}
