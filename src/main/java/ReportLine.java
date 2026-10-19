import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class ReportLine {
    /**
     * Строка вида "40;DUPLICATE;Найдены дубли в списках выдачи;9".
     * Поля через ';': bookId, имя константы типа (name()), текст типа (title()),
     * читатели через запятую без пробелов. Если читателей нет, последнее поле пустое.
     */
    public static String toLine(LibraryProblem problem) {
        String readers = problem.readerIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return problem.bookId() + ";"
                + problem.type().name() + ";"
                + problem.type().title() + ";"
                + readers;
    }

    /**
     * Обратно из строки в LibraryProblem. Тип ищи по имени константы через ProblemType.valueOf,
     * текст типа при чтении не нужен. Если полей не 4 или тип неизвестен, брось IllegalArgumentException.
     */
    public static LibraryProblem fromLine(String line) {

        String[] parts = line.split(";", -1);

        if (parts.length != 4) {
            throw new IllegalArgumentException("Ожидается 4 поля.");
        }

        int bookId = Integer.parseInt(parts[0]);
        ProblemType type = ProblemType.valueOf(parts[1]);

        List<Integer> readerIds = parts[3].isEmpty()
                ? List.of()
                : Arrays.stream(parts[3].split(","))
                .map(Integer::parseInt)
                .toList();

        return new LibraryProblem(type, bookId, readerIds);
    }
}