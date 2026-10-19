import java.util.Comparator;

public class ReportOrder {

    /** По bookId по возрастанию, при равном bookId по типу в порядке объявления констант ProblemType. */
    public static Comparator<LibraryProblem> byBookAndType() {
        return Comparator.comparingInt(LibraryProblem::bookId)
                .thenComparing(LibraryProblem::type);
    }

    /** Сначала проблемы, где больше читателей. При равном числе читателей порядок как в byBookAndType. */
    public static Comparator<LibraryProblem> byReadersCount() {
        Comparator<LibraryProblem> comparator = Comparator
                .comparingInt((LibraryProblem p) -> p.readerIds().size())
                .reversed();

        return comparator.thenComparing(byBookAndType());
    }
}