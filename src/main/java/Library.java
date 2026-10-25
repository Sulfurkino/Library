import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class Library {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Reader> readers = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public void removeBook(Book book) {
        books.remove(book.getId());
    }

    public Optional<Book> getBookById(int bookId) {
        return Optional.ofNullable(books.get(bookId));
    }

    public Optional<Book> findBookById(int bookId) {
        return Optional.ofNullable(books.get(bookId));
    }

    public Optional<Book> findBookByName(String bookName) {
        if (bookName == null) {
            return Optional.empty();
        }

        String normalizedName = bookName.trim();

        return books.values().stream()
                .filter(book -> book.getBookName() != null)
                .filter(book -> book.getBookName().trim().equalsIgnoreCase(normalizedName))
                .sorted(Comparator.comparingInt(Book::getId))
                .findFirst();
    }

    //защита от null
    public void returnBook(int bookId, int readerId) {
        Book book = getBookById(bookId).orElse(null);
        Reader reader = getReaderById(readerId);

        if (book == null || book.isAvailable() || reader == null) {
            return;
        }

        if (!reader.getBooks().contains(book)) {
            return;
        }

        reader.getBooks().remove(book);
        book.setAvailable(true);
    }

    //защита от null
    public void borrowBook(int bookId, int readerId) {
        Book book = getBookById(bookId).orElse(null);
        Reader reader = getReaderById(readerId);

        if (book == null || !book.isAvailable() || reader == null) {
            return;
        }

        reader.getBooks().add(book);
        book.setAvailable(false);
    }

    public void addBooks(List<Book> books) {
        for (Book book : books) {
            addBook(book);
        }
    }

    private List<Book> booksById() {
        return books.values().stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .toList();
    }

    public List<Book> getAvailableBooks() {
        return booksById().stream()
                .filter(Book::isAvailable)
                .toList();
    }

    public Reader getReaderById(int readerId) {
        return readers.get(readerId);
    }

    public List<Book> getBorrowedBooks() {
        return booksById().stream()
                .filter(book -> !book.isAvailable())
                .toList();
    }

    public List<Integer> getBorrowedBooksId() {
        return getBorrowedBooks().stream()
                .map(Book::getId)
                .toList();
    }

    public List<Integer> getAvailableBooksId() {
        return getAvailableBooks().stream()
                .map(Book::getId)
                .toList();
    }

    public Map<Integer, BookReadersStats> getBookReadersStats() {
        Map<Integer, BookReadersStats> result = new HashMap<>();

        for (Reader reader : readers.values()) {
            for (Book book : reader.getBooks()) {
                result
                        .computeIfAbsent(book.getId(), id -> new BookReadersStats())
                        .add(reader.getId());
            }
        }

        return result;
    }

    public List<Book> getAllBooks() {
        return booksById();
    }

    public List<Reader> getAllReaders() {
        return new ArrayList<>(readers.values());
    }
}

