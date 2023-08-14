package com.example.bookwonders.repository.book;

import com.example.bookwonders.dto.book.BookSearchParametersDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.repository.SpecificationBuilder;
import com.example.bookwonders.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR)
                    .getSpecification(bookSearchParameters.authors()));
        }
        if (bookSearchParameters.titles() != null && bookSearchParameters.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TITLE)
                    .getSpecification(bookSearchParameters.titles()));
        }
        return specification;
    }
}
