import model.Book;
import model.Library;
import org.junit.jupiter.api.Test;
import service.Manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagerSearchTest {
    @Test
    void findsNormalizedNameWithSmallestId() {
        Library library = new Library();
        library.addBook(new Book("Java", 33, 2020));
        library.addBook(new Book(" JAVA ", 7, 2021));
        Manager manager = new Manager(library);

        var actual = manager.findBookByName(" jaVA ")
                .map(Book::getId);

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }

    @Test
    void missingNameReturnsEmpty() {
        Manager manager = new Manager(new Library());

        var actual = manager.findBookByName("Spring");

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }
}
