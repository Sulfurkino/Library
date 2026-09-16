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

    public void returnBook(int bookId) {
        Book book = getBookById(bookId);

        if (book == null || book.isAvailable()) {
            return;
        }

        book.setAvailable(true);
    }

    public void borrowBook(int bookId) {
        Book book = getBookById(bookId);

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
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

}

