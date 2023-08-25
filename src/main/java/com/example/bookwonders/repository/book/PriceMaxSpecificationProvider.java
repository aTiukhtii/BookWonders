package com.example.bookwonders.repository.book;

import com.example.bookwonders.model.Book;
import com.example.bookwonders.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceMaxSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_NAME = "price";
    private static final String FILTER_KEY = "priceTo";

    @Override
    public String getKey() {
        return FILTER_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(FIELD_NAME), params[0]);
    }
}
