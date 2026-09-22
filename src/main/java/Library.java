import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class Library {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Reader> readers = new HashMap<>();

    public void addBook(Book book){
        books.put(book.getId(),book);
    }

    public void removeBook(Book book){
        books.remove(book.getId());
    }

    public Book getBookById(int bookId){
       return books.get(bookId);
    }

    public Book getBookByName(String bookName){
        for (Map.Entry<Integer,Book> books : books.entrySet()){
            if (Objects.equals(books.getValue().getBookName(), bookName)) {
                return books.getValue();
            }
        }
        return null;
    }
    //защита от null
    public void returnBook(int bookId, int readerId) {
        Book book = getBookById(bookId);
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
        Book book = getBookById(bookId);
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

    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                result.add(book);
            }
        }
        return result;
    }

    public Reader getReaderById(int readerId) {
        return readers.get(readerId);
    }

    public List<Book> getBorrowedBooks() {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isAvailable()) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Integer> getBorrowedBooksId() {
        List<Integer> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isAvailable()) {
                result.add(book.getId());
            }
        }
        return result;
    }

    public List<Integer> getAvailableBooksId() {
        List<Integer> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                result.add(book.getId());
            }
        }
        return result;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Reader> getAllReaders() {
        return new ArrayList<>(readers.values());
    }
}

