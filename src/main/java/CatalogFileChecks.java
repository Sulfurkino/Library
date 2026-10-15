import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

/** Проверки CatalogFile на временных файлах. Запусти main: при расхождении программа падает. */
public class CatalogFileChecks {
    public static void main(String[] args) throws IOException {
        Path dir = Files.createTempDirectory("library-checks");

        // 1. Хорошие строки принимаются, плохая попадает в ошибки с номером строки.
        Path catalog = dir.resolve("catalog.csv");
        Files.write(catalog, List.of("id;name;year;available", "1;Война и мир;1869;true",
                "2;Мастер и Маргарита;1967;false", "", "3;Чистый код;двадцать;true"), StandardCharsets.UTF_8);
        Library library = new Library();
        ImportResult result = CatalogFile.load(catalog, library);
        expect("принятые id", List.of(1, 2), result.importedIds());
        expect("ошибки", List.of(new ImportError(5, ImportError.Reason.BAD_YEAR)), result.errors());
        expect("название с кириллицей", "Мастер и Маргарита", library.getBookById(2).getBookName());
        expect("доступность из файла", false, library.getBookById(2).isAvailable());

        // 2. Книга с таким id уже в каталоге — не заменяем её.
        Files.write(catalog, List.of("id;name;year;available", "1;Подмена;2000;true"), StandardCharsets.UTF_8);
        ImportResult again = CatalogFile.load(catalog, library);
        expect("повтор id из каталога", List.of(new ImportError(2, ImportError.Reason.DUPLICATE_ID)), again.errors());
        expect("существующая книга осталась", "Война и мир", library.getBookById(1).getBookName());

        // 3. Нет файла — IOException уходит вызывающему.
        try {
            CatalogFile.load(dir.resolve("missing.csv"), new Library());
            throw new AssertionError("ожидали NoSuchFileException");
        } catch (NoSuchFileException expected) {
            System.out.println("PASS нет файла");
        }

        // 4. Отчёт: заголовок, порядок по книге и типу, name() типа; повторная запись перезаписывает файл.
        Path report = dir.resolve("problems.csv");
        List<LibraryProblem> problems = List.of(
                new LibraryProblem(ProblemType.MISSING_IN_CATALOG, 50, List.of(9, 2)),
                new LibraryProblem(ProblemType.DUPLICATE, 50, List.of(2, 9)),
                new LibraryProblem(ProblemType.DUPLICATE, 40, List.of(9)));
        CatalogFile.writeReport(report, problems);
        CatalogFile.writeReport(report, problems);
        List<String> lines = Files.readAllLines(report, StandardCharsets.UTF_8);
        expect("строк в отчёте", 4, lines.size());
        expect("заголовок", "bookId;type;title;readers", lines.get(0));
        expect("первая проблема", "40;DUPLICATE;" + ProblemType.DUPLICATE.title() + ";9", lines.get(1));
        expect("читатели через запятую", "50;MISSING_IN_CATALOG;" + ProblemType.MISSING_IN_CATALOG.title() + ";2,9", lines.get(3));
        System.out.println("ALL CatalogFile CHECKS PASSED");
    }

    private static void expect(String name, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + "\n  ожидали: " + expected + "\n  получили: " + actual);
        }
        System.out.println("PASS " + name);
    }
}
