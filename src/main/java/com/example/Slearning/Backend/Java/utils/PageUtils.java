package com.example.Slearning.Backend.Java.utils;

import com.example.Slearning.Backend.Java.domain.mappers.GenericMapper;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;

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

    public static <E, D> PageResponse<D> paging(
            Page<E> page,
            Integer pageNumber,
            Integer pageSize
    ){
        Collection<E> content = page.getContent();
        GenericMapper<E, D> genericMapper = new GenericMapper<E, D>();
        Collection<D> contentDto = genericMapper.toDtoCollection(content);

        PageResponse<D> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(contentDto);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }
}
