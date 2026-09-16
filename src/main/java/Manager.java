import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Manager {

    private Library library;
    private int id = 0;

    public Manager(Library library) {
        this.library = library;
    }

    public int generateId() {
        return id++;
    }

    public void addBook(String bookName, int publishYear) {
        int bookId = generateId();

        Book newBook = new Book(bookName, bookId, publishYear);

        library.getBooks().put(bookId, newBook);
    }

    public void removeBook(Book bookToRemove) {
        library.getBooks().remove(bookToRemove.getId());
    }

    public Book findBookById(int bookId) {
        if (library.getBooks().containsKey(bookId)) {
            return library.getBooks().get(bookId);
        }
        return null;
    }

    public Book findBookByName(String bookName) {
        for (Map.Entry<Integer, Book> books : library.getBooks().entrySet()) {
            if (Objects.equals(books.getValue().getBookName(), bookName)) {
                return books.getValue();
            }
        }
        return null;
    }

    public void returnBook(int bookId) {
        Book book = findBookById(bookId);

        if (book == null || book.isAvailable()) {
            return;
        }

        book.setAvailable(true);
    }

    public void borrowBook(int bookId) {
        Book book = findBookById(bookId);

        if (book == null) {
            return;
        }

        if (!book.isAvailable()) {
            return;
        }

        book.setAvailable(false);
    }

    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Map.Entry<Integer, Book> books : library.getBooks().entrySet()) {
            if (books.getValue().isAvailable()) {
                result.add(books.getValue());
            }
        }
        return result;
    }

    public List<Book> getAllBooks() {
        List<Book> result = new ArrayList<>();
        for (Map.Entry<Integer, Book> books : library.getBooks().entrySet()) {
            result.add(books.getValue());
        }
        return result;
    }


}
