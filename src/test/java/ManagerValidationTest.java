import model.*;
import org.junit.jupiter.api.Test;
import service.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagerValidationTest {
    @Test
    void returnsAllProblemsInBookAndEnumOrder() {
        Library library = libraryWithAllProblems();
        Manager manager = new Manager(library);

        // TODO: составь ожидаемые проблемы.
        List<LibraryProblem> expected = List.of(
                new LibraryProblem(
                        ProblemType.AVAILABLE_BUT_BORROWED,
                        7,
                        List.of(2)),
                new LibraryProblem(
                        ProblemType.UNAVAILABLE_NOT_BORROWED,
                        12,
                        List.of(9)
                ),
                new LibraryProblem(
                        ProblemType.DUPLICATE,
                        33,
                        List.of(2,9)
                ),
                new LibraryProblem(
                        ProblemType.AVAILABLE_BUT_BORROWED,
                        33,
                        List.of(2,9)),
                new LibraryProblem(
                        ProblemType.DUPLICATE,
                        40,
                        List.of(9)
                ),
                new LibraryProblem(
                        ProblemType.MISSING_IN_CATALOG,
                        55,
                        List.of(2)
                )
                );

        var actual = manager.validateLibrary();

        assertEquals(expected,actual);
    }

    @Test
    void emptyLibraryHasNoProblems() {
        Manager manager = new Manager(new Library());

        var actual = manager.validateLibrary();

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }

    @Test
    void consistentLibraryHasNoProblems() {
        Library library = new Library();
        book(library, "Доступна", 7, true);
        Book borrowed = book(library, "Выдана", 12, false);
        Reader reader = new Reader(
                new ArrayList<>(List.of(borrowed)), 2, "Два");
        library.setReaders(Map.of(2, reader));
        Manager manager = new Manager(library);

        var actual = manager.validateLibrary();

        // TODO: сравни ожидаемое с actual.
        throw new AssertionError(
                "TODO: допиши assertEquals");
    }

    // Данные уже подготовлены. Перечисли проблемы сам.
    private Library libraryWithAllProblems() {
        Library library = new Library();
        book(library, "Война и мир", 7, true);
        book(library, "Идиот", 12, false);
        book(library, "Чистый код", 33, true);
        Book book40 = book(library, "Рефакторинг", 40, false);
        Book missing = new Book("Нет в каталоге", 55, 2000);

        // Другие объекты тех же книг: сравниваем по id.
        Book copy7 = new Book("Война и мир", 7, 1869);
        Book copy33 = new Book("Чистый код", 33, 2008);
        Reader reader2 = new Reader(new ArrayList<>(
                List.of(copy7, copy33, missing)), 2, "Два");
        Reader reader9 = new Reader(new ArrayList<>(
                List.of(copy33, book40, book40)), 9, "Девять");
        library.setReaders(Map.of(9, reader9, 2, reader2));
        return library;
    }

    private Book book(Library library, String name,
                      int id, boolean available) {
        Book book = new Book(name, id, 2000);
        book.setAvailable(available);
        library.addBook(book);
        return book;
    }
}
