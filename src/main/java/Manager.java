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

        library.addBook(newBook);
    }

    public void removeBook(Book bookToRemove) {
        library.removeBook(bookToRemove);
    }

    public Book getBookById(int bookId) {
        return library.getBookById(bookId);
    }

    public Book getBookByName(String bookName) {
        return library.getBookByName(bookName);
    }

    public void returnBook(int bookId) {
       library.returnBook(bookId);
    }

    public void borrowBook(int bookId) {
        library.borrowBook(bookId);
    }

    public List<Book> getAvailableBooks() {
        return library.getAvailableBooks();
    }

    public List<Book> getAllBooks() {
        return library.getAllBooks();
    }


}
