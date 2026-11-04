package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import model.BookView;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class SearchResult {
    private final List<BookView> books;
    private final int total;
}