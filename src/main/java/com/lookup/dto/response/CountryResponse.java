package com.lookup.dto.response;

import lombok.Builder;

@Builder
public record CountryResponse(
    Long id,
    String name
) {
}
