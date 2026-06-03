package com.aycom.feedback_app.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> content, PageMeta page) {

    public record PageMeta(int number, int size, long totalElements, int totalPages) {
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                new PageMeta(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
