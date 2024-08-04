package com.lookup.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CountryResponse(
    Long id,
    String name,
    String capital,
    LocalDateTime createdDate
) {
}
