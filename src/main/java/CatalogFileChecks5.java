import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Проверки пятого занятия: заголовок, плохие строки и отчёт. Запусти main: при расхождении программа упадёт. */
public class CatalogFileChecks5 {
    private static final String HEADER = "id;name;year;available";

    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("library-checks5");
        Path catalog = dir.resolve("catalog.csv");

        // 1. Колонки берём по заголовку, а не по месту.
        Library library = new Library();
        ImportResult result = load(catalog, library, "name;year;available;id", "Война и мир;1869;false;1");
        expect("колонки по заголовку", List.of(1), result.importedIds());
        expect("название из колонки name", "Война и мир", library.getBookById(1).getBookName());
        expect("доступность из колонки available", false, library.getBookById(1).isAvailable());

        // 2. Строка «id;...» в середине файла — это данные, а не заголовок.
        result = load(catalog, new Library(), HEADER, "1;A;2000;true", "id;name;year;available", "2;B;2001;false");
        expect("строка id в середине", List.of(new ImportError(3, ImportError.Reason.BAD_ID)), result.errors());

        // 3. Короткая и длинная строки дают FIELD_COUNT и не роняют импорт.
        result = load(catalog, new Library(), HEADER, "1;Java;2020;true", "2;Spring", "3;SQL;1999;false", "4;A;2000;true;лишнее");
        expect("принятые рядом с плохими", List.of(1, 3), result.importedIds());
        expect("короткая и длинная строки", List.of(new ImportError(3, ImportError.Reason.FIELD_COUNT),
                new ImportError(5, ImportError.Reason.FIELD_COUNT)), result.errors());

        // 4. Пробелы вокруг полей обрезаем, строку из пробелов пропускаем.
        library = new Library();
        result = load(catalog, library, HEADER, "  7 ;  Чистый код ; 2008 ; TRUE ", "   ", "8;SQL;1999;false");
        expect("пробелы вокруг полей", List.of(7, 8), result.importedIds());
        expect("название без пробелов", "Чистый код", library.getBookById(7).getBookName());
        expect("строка из пробелов", List.of(), result.errors());

        // 5. Отрицательный год допустим: Илиада написана до нашей эры.
        result = load(catalog, new Library(), HEADER, "10;Илиада;-750;true");
        expect("отрицательный год", List.of(10), result.importedIds());

        // 6. Причина — первая сработавшая проверка: пустое имя раньше повтора id.
        result = load(catalog, new Library(), HEADER, "1;A;2000;true", "1;;2000;true");
        expect("порядок причин", List.of(new ImportError(3, ImportError.Reason.EMPTY_NAME)), result.errors());

        // 7. Чужой заголовок и пустой файл: импорт не начинаем.
        Library untouched = new Library();
        expectThrows("неверный заголовок", IllegalArgumentException.class,
                () -> load(catalog, untouched, "id;title;year;available", "1;A;2000;true"));
        expect("каталог не тронут", true, untouched.getBooks().isEmpty());
        expectThrows("пустой файл", IllegalArgumentException.class, () -> load(catalog, new Library()));

        // 8. Отчёт: тип в порядке enum, bookId как число, пустой отчёт.
        Path report = dir.resolve("problems.csv");
        CatalogFile.writeReport(report, List.of(
                new LibraryProblem(ProblemType.AVAILABLE_BUT_BORROWED, 20, List.of(1)),
                new LibraryProblem(ProblemType.DUPLICATE, 100, List.of(1)),
                new LibraryProblem(ProblemType.DUPLICATE, 20, List.of(3, 1))));
        List<String> lines = Files.readAllLines(report, StandardCharsets.UTF_8);
        expect("тип в порядке enum", "20;DUPLICATE;" + ProblemType.DUPLICATE.title() + ";1,3", lines.get(1));
        expect("потом другой тип той же книги", "20;AVAILABLE_BUT_BORROWED;" + ProblemType.AVAILABLE_BUT_BORROWED.title() + ";1", lines.get(2));
        expect("bookId как число", "100;DUPLICATE;" + ProblemType.DUPLICATE.title() + ";1", lines.get(3));
        CatalogFile.writeReport(report, List.of());
        expect("пустой отчёт", List.of("bookId;type;title;readers"), Files.readAllLines(report, StandardCharsets.UTF_8));

        // 9. Что записали, то и прочитали, уже в порядке отчёта. Пока readReport бросает UnsupportedOperationException, проверку пропускаем.
        List<LibraryProblem> problems = List.of(
                new LibraryProblem(ProblemType.MISSING_IN_CATALOG, 50, List.of(9, 2)),
                new LibraryProblem(ProblemType.DUPLICATE, 50, List.of(2, 9)),
                new LibraryProblem(ProblemType.DUPLICATE, 40, List.of(9)));
        CatalogFile.writeReport(report, problems);
        try {
            expect("отчёт читается обратно", List.of(problems.get(2), problems.get(1), problems.get(0)), CatalogFile.readReport(report));
        } catch (UnsupportedOperationException notYet) {
            System.out.println("SKIP отчёт читается обратно: readReport ещё не сделан");
        }

        // 10. Папки нет — ошибку видит вызывающий, файл не появляется.
        Path lost = dir.resolve("нет-такой-папки").resolve("problems.csv");
        expectThrows("отчёт в несуществующую папку", IOException.class, () -> CatalogFile.writeReport(lost, problems));
        expect("файл не создан", false, Files.exists(lost));

        System.out.println("ALL CatalogFileChecks5 PASSED");
    }

    private static ImportResult load(Path catalog, Library library, String... lines) throws IOException {
        Files.write(catalog, List.of(lines), StandardCharsets.UTF_8);
        return CatalogFile.load(catalog, library);
    }

    private static void expect(String name, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + "\n  ожидали: " + expected + "\n  получили: " + actual);
        }
        System.out.println("PASS " + name);
    }

    /** Действие, которое может бросить исключение. Один абстрактный метод, поэтому сюда подходит лямбда. */
    private interface Action {
        void run() throws Exception;
    }

    private static void expectThrows(String name, Class<? extends Exception> type, Action action) throws Exception {
        try {
            action.run();
        } catch (Exception error) {
            if (type.isInstance(error)) {
                System.out.println("PASS " + name + " (" + error.getClass().getSimpleName() + ": " + error.getMessage() + ")");
                return;
            }
            throw new AssertionError(name + "\n  ожидали: " + type.getSimpleName() + "\n  получили: " + error);
        }
        throw new AssertionError(name + "\n  ожидали: " + type.getSimpleName() + "\n  получили: исключения нет");
    }
}
