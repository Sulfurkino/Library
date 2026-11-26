package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class Book {

    private int id;
    private String bookName;
    private int publishYear;
    private boolean isAvailable;
    private LocalDate returnDeadline;

    public Book(String bookName, int id, int publishYear) {
        this.bookName = bookName;
        this.id = id;
        this.isAvailable = true;
        this.publishYear = publishYear;
    }

    public LocalDate getDueDate() {
        if (returnDeadline == null){
            return null;
        }
        return returnDeadline;
    }
}

