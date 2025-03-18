package com.ra.inventory_management.util;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
    public static Pageable create(Integer pageNum, Integer pageSize, String sortBy, String sortType) {
        Sort sort = sortType.equals("asc") ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        return PageRequest.of(pageNum, pageSize, sort);
    }
}
