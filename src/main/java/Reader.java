import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Reader {
    private int id;
    private String name;
    private List<Book> books;

    public Reader(List<Book> bookList, int id, String name) {
        this.books = bookList;
        this.id = id;
        this.name = name;
    }
}
