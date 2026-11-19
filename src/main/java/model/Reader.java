package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Reader {
    private Integer id;
    private String name;
    private List<Book> books;
    private List<Integer> history;

    public Reader(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.books = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Reader{" +
                "books=" + books +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", history=" + history +
                '}';
    }
}
