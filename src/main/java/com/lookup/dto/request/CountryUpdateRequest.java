package com.lookup.dto.request;

public record CountryUpdateRequest(
    Long id,
    String name,
    String capital
) {
}
