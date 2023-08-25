package com.example.bookwonders.repository.book;

import com.example.bookwonders.model.Book;
import com.example.bookwonders.repository.SpecificationManager;
import com.example.bookwonders.repository.SpecificationProvider;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationManager implements SpecificationManager<Book> {
    private final Map<String, SpecificationProvider<Book>> providersMap;

    public BookSpecificationManager(List<SpecificationProvider<Book>> specifications) {
        providersMap = specifications.stream()
                .collect(Collectors.toMap(SpecificationProvider::getKey, Function.identity()));

    }

    @Override
    public Specification<Book> get(String filterKey, String[] params) {
        if (!providersMap.containsKey(filterKey)) {
            throw new RuntimeException("Key " + filterKey + " is not supported");
        }
        return providersMap.get(filterKey).getSpecification(params);
    }
}
