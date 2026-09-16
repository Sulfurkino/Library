import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {

    private int id;
    private String bookName;
    private int publishYear;
    private boolean isAvailable;

    public Book(String bookName, int id, int publishYear) {
        this.bookName = bookName;
        this.id = id;
        this.isAvailable = true;
        this.publishYear = publishYear;
    }
}

