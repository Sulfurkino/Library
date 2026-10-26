import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatusRules {
    public static List<LibraryProblem> find(List<Book> books, List<Reader> readers) {
        Map<Integer, List<Integer>> readersByBookId = readers.stream()
                .flatMap(reader -> reader.getBooks().stream()
                        .map(book -> Map.entry(book.getId(), reader.getId())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return books.stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .filter(book -> {
                    List<Integer> readerIds = readersByBookId.getOrDefault(book.getId(), List.of());

                    return (book.isAvailable() && !readerIds.isEmpty())
                            || (!book.isAvailable() && readerIds.isEmpty());
                })
                .map(book -> {
                    List<Integer> readerIds = readersByBookId.getOrDefault(book.getId(), List.of());

                    ProblemType type = book.isAvailable()
                            ? ProblemType.AVAILABLE_BUT_BORROWED
                            : ProblemType.UNAVAILABLE_NOT_BORROWED;

                    return new LibraryProblem(
                            type,
                            book.getId(),
                            readerIds
                    );
                })
                .toList();
    }
}