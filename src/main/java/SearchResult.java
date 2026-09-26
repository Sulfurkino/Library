import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchResult {
    private final List<BookView> books;
    private final int total;
}