package com.ngntu10.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;


public class PageableUtil {
    public static Pageable getPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", "20"));

        String sortBy = params.getOrDefault("sortBy", "createdAt");
        String sortDir = params.getOrDefault("sortDir", "desc");

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }
}
