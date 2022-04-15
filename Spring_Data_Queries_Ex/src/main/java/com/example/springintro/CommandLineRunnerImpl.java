package com.example.springintro;

import com.example.springintro.model.BookSummary;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        //seedData();
        //13
        int count = Integer.parseInt(scanner.nextLine());
        int delete = this.bookService.deleteWithCopiesLessThan(count);
        System.out.println(delete);
        //12 не парсва датата прарвилно
       // String date = scanner.nextLine();
       // int copies = Integer.parseInt(scanner.nextLine());
       // int i = this.bookService.addCopiesToBooksAfter(date, copies);
       // System.out.printf("%d books are released after %s, so total of %d book copies were added", i, date, i*copies);
        //11
        //String title = scanner.nextLine();
        //BookSummary summary = this.bookService.getInformationByTitle(title);
        //System.out.println(summary.getTitle() + " " + summary.getEditionType() + " " + summary.getAgeRestriction()
        //+ " " + summary.getPrice());;
        //10
        //this.authorService.getWithTotalCopies().forEach(a-> System.out.println(a.getFirstName() + " " + a.getLastName() + " - " + a.getTotalCopies()));
        //9
        // int length = Integer.parseInt(scanner.nextLine());
        // int count = this.bookService.countBooksWithTitleLongerThan(length);
        // System.out.println("There are " + count + " books with longer title than"+  length+ " symbols");
        //8
        //this.bookService.selectByAuthorLastNameStartingWith("Ric").forEach(
        //      b-> System.out.println(b.getTitle() + " (" + b.getAuthor().getFirstName() + " " + b.getAuthor().getLastName() + ")"));
        //7
        //this.bookService.selectByTitleContaining("WOR")
        //      .forEach(b-> System.out.println(b.getTitle()));
        //6
        //this.authorService.selectByFirstNameEndingWith("e").forEach(a-> System.out.println(
        //       a.getFirstName() + " " + a.getLastName()));
        //5
        //this.bookService.selectAllBooksReleasedBefore("12-04-1992").forEach(b->
        //        System.out.println(b.getTitle() + " " + b.getEditionType() + " " + b.getPrice()));
        //4
        //this.bookService.selectBooksReleasedBefore(2000).forEach(System.out::println);
        //3
        // this.bookService.selectTitleAndPriceByPriceLessThanOrGreaterThan(5, 40)
        //       .forEach(b-> System.out.println(b.getTitle() + " - $" + b.getPrice()));
        //2
        //this.bookService.selectAllBooksByEditionTypeAndCopies(EditionType.GOLD, 5000).forEach(System.out::println);
        //01
        // this.bookService.findAllBooksByAgeRestriction("miNor").forEach(System.out::println);

        //printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //   printAllAuthorsAndNumberOfTheirBooks();
        //pritnALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

    }

    private void pritnALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
