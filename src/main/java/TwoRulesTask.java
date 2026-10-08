import java.util.*;

public class TwoRulesTask {
    public List<LibraryProblem> findProblems(Library library) {

        Map<Integer, BookReadersStats> statsMap =
                new TreeMap<>(library.getBookReadersStats());

        List<LibraryProblem> result = new ArrayList<>();

        for (Map.Entry<Integer, BookReadersStats> entry : statsMap.entrySet()) {
            Integer bookId = entry.getKey();
            BookReadersStats bookReadersStats = entry.getValue();

            List<Integer> sortedReaders = bookReadersStats.getReaderIds().stream()
                    .sorted()
                    .toList();
            //-1
            if (bookReadersStats.getOccurrences() > 1) {
                result.add(new LibraryProblem(
                        "Найдены дубли в списке выдачи",
                        Set.of(bookId),
                        new LinkedHashSet<>(sortedReaders)
                ));
            }
            //-2
            if (!library.getBooks().containsKey(bookId)) {
                result.add(new LibraryProblem(
                        "У читателя есть книга, отсутствующая в каталоге",
                        Set.of(bookId),
                        new LinkedHashSet<>(sortedReaders)
                ));
            }
        }

        result.sort(
                Comparator.comparing((LibraryProblem problem) ->
                                problem.getBookId().iterator().next())
                        .thenComparing(LibraryProblem::getExceptionType)
        );

        return result;
    }
}
