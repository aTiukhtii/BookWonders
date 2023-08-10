package com.example.bookwonders.repository;

import com.example.bookwonders.dto.book.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto bookSearchParameters);
}
