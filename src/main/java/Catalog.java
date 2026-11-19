import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Catalog {
    public record Book(int id, String title) {}

    private final List<Book> books;

    public Catalog(List<Book> books) {
        this.books = List.copyOf(books);
    }

    public Optional<Book> findById(int id) {
        return books.stream()
                .filter(book -> book.id() == id)
                .findFirst();
    }

    public Optional<Book> findByName(String name) {
        String query = name.trim();
        return books.stream()
                .filter(book -> book.title().trim()
                        .equalsIgnoreCase(query))
                .min(Comparator.comparingInt(Book::id));
    }

    public List<Book> allBooks() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::id))
                .toList();
    }
}