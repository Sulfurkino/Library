//import lombok.Getter;
//import lombok.AllArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;


import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
//import java.util.Set;
//@ToString
//@AllArgsConstructor
//public class LibraryProblem {
//    private String exceptionType;
//    private Set<Integer> bookId;
//    private Set<Integer> readerIds;
//
//}


/** Часть 04/06. Одна проблема Library: тип, одна книга и её читатели. */
public record LibraryProblem(ProblemType type, int bookId, List<Integer> readerIds) {
    public LibraryProblem {
        // TODO: компактный конструктор.
        // 1. type и readerIds не null — Objects.requireNonNull.
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(readerIds, "readerIds");

        // 2. bookId < 0 — IllegalArgumentException.
        if (bookId < 0){
            throw new IllegalArgumentException("bookId не должен быть отрицательным.");
        }

        // 3. readerIds — неизменяемая копия без повторов, по возрастанию.
        readerIds = List.copyOf(new TreeSet<>(readerIds));
        //    После создания записи изменение исходного списка не должно её менять.

    }
}