package com.example.springintro.repository;

import com.example.springintro.model.BookSummary;
import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findTitleByAgeRestriction(AgeRestriction afeRestriction);

    List<Book> findAllBookByEditionTypeAndCopiesLessThan(EditionType gold, int copies);

    List<Book> findTitleAndPriceByPriceLessThanOrPriceGreaterThan(BigDecimal lowerThan, BigDecimal higherThan);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);

    List<Book> findByTitleContaining(String containStr);

    List<Book> findByAuthorLastNameStartingWith(String search);

    @Query("SELECT COUNT(b) FROM Book b WHERE length(b.title) > :length")
    int countBooksWithTitleLongerThan(int length);

    @Query("SELECT b.title as title, b.editionType as editionType, b.ageRestriction as ageRestriction," +
            " b.price as price FROM Book b" +
            " WHERE b.title = :title")
    BookSummary getInformationByTitle(String title);

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.copies = b.copies + :amount WHERE b.releaseDate > :after")
    int addCopiesToBooksAfter(LocalDate after, int amount);

    @Transactional
    int deleteByCopiesLessThan(int count);
}
