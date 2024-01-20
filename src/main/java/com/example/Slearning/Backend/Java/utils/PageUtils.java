package com.example.Slearning.Backend.Java.utils;

import com.example.Slearning.Backend.Java.domain.mappers.GenericMapper;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

public class PageUtils {
    public static Pageable getPageable(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return pageable;
    }
}
