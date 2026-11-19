import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {
    @Test
    void missingIdReturnsEmpty() {
        Catalog catalog = new Catalog(List.of(
                new Catalog.Book(12, "Dune")
        ));

        Optional<Catalog.Book> actual =
                catalog.findById(90);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void foundIdKeepsTheBook() {
        Catalog catalog = new Catalog(List.of(
                new Catalog.Book(12, "Dune")
        ));

        Optional<Catalog.Book> actual =
                catalog.findById(12);

        assertEquals(
                Optional.of(new Catalog.Book(12, "Dune")),
                actual
        );
    }
    @Test
    void nameSearchNormalizesAndChoosesSmallestId() {
        Catalog catalog = new Catalog(List.of(
                new Catalog.Book(40, "  DUNE  "),
                new Catalog.Book(12, " Dune "),
                new Catalog.Book(25, "Other")
        ));

        Optional<Integer> actual = catalog
                .findByName("  dune  ")
                .map(Catalog.Book::id);

        assertEquals(Optional.of(12), actual);
    }
    @Test
    void allBooksKeepsValuesAndSortsById() {
        Catalog catalog = new Catalog(List.of(
                new Catalog.Book(25, "Other"),
                new Catalog.Book(12, "Dune")
        ));
        List<Catalog.Book> expected = List.of(
                new Catalog.Book(12, "Dune"),
                new Catalog.Book(25, "Other")
        );

        List<Catalog.Book> actual = catalog.allBooks();

        assertEquals(expected, actual);
    }
}