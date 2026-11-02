import model.Book;
import model.Library;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibrarySearchTest {
    @Test
    void missingBookReturnsEmpty() {
        Library library = new Library();

        var actual = library.findBookById(99);

        assertEquals(Optional.empty(),actual);
    }

    @Test
    void missingNameReturnsEmpty() {
        Library library = new Library();
        library.addBook(new Book("Java", 7, 2020));

        var actual = library.findBookByName("Spring");

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }

    @Test
    void nameIgnoresCaseAndOuterSpaces() {
        Library library = new Library();
        library.addBook(new Book("  Java  ", 7, 2020));

        var actual = library.findBookByName(" jaVA ")
                .map(Book::getId);

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }

    @Test
    void sameNameChoosesSmallestId() {
        Library library = new Library();
        // Без сортировки 33 может оказаться раньше 7.
        library.addBook(new Book("Java", 33, 2020));
        library.addBook(new Book("JAVA", 7, 2021));

        var actual = library.findBookByName("Java")
                .map(Book::getId);

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }

    @Test
    void foundBookKeepsId() {
        Library library = new Library();
        library.addBook(new Book("Java", 7, 2020));

        var actual = library.findBookById(7)
                .map(Book::getId);

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }
}
