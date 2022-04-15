package com.example.springintro.service;

import com.example.springintro.model.BookSummary;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);
    List<String> findAllBooksByAgeRestriction(String ageRestriction);


    List<String> selectAllBooksByEditionTypeAndCopies(EditionType gold, int copies);

    List<Book> selectTitleAndPriceByPriceLessThanOrGreaterThan(float lower, float higher );

    List<String> selectBooksReleasedBefore(int date);

    List<Book> selectAllBooksReleasedBefore(String date);

    List<Book> selectByTitleContaining(String containStr);

    List<Book> selectByAuthorLastNameStartingWith(String search);

    int countBooksWithTitleLongerThan(int length);

    BookSummary getInformationByTitle(String title);

    int addCopiesToBooksAfter(String date, int copies);

    int deleteWithCopiesLessThan(int count);
}
