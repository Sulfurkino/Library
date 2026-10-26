import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Проверки шестого занятия: Library на потоках, Optional и весь validateLibrary. Запусти main: при расхождении программа упадёт. */
public class LibraryChecks6 {
    public static void main(String[] args) {
        // Каталог: 7 и 33 доступны, 12 и 40 выданы.
        Library library = new Library();
        Book book33 = book(library, "Чистый код", 33, 2008, true);
        Book book7 = book(library, "Война и мир", 7, 1869, true);
        book(library, "Идиот", 12, 1869, false);
        Book book40 = book(library, "Рефакторинг", 40, 2018, false);
        Book book55 = new Book("Нет в каталоге", 55, 2000);

        // 1. Четыре метода Library: id по возрастанию.
        expect("id доступных книг", List.of(7, 33), library.getAvailableBooksId());
        expect("id выданных книг", List.of(12, 40), library.getBorrowedBooksId());
        expect("доступные книги по id", List.of(7, 33), ids(library.getAvailableBooks()));
        expect("выданные книги по id", List.of(12, 40), ids(library.getBorrowedBooks()));

        // 2. Optional вместо null.
        expect("findBookById нашёл", Optional.of("Война и мир"), library.findBookById(7).map(Book::getBookName));
        expect("findBookById не нашёл", Optional.empty(), library.findBookById(99));
        expect("findBookByName без регистра и пробелов", Optional.of(33), library.findBookByName("  чистый КОД ").map(Book::getId));
        expect("findBookByName не нашёл", Optional.empty(), library.findBookByName("Обломов"));
        expect("пустая библиотека", Optional.empty(), new Library().findBookById(1));

        // 3. Весь validateLibrary: TwoRulesTask и две средние ветки, по книге и типу.
        Reader reader2 = new Reader(new ArrayList<>(List.of(book7, book33, book55)), 2, "Второй");
        Reader reader9 = new Reader(new ArrayList<>(List.of(book33, book40, book40)), 9, "Девятый");
        library.setReaders(Map.of(2, reader2, 9, reader9));
        List<LibraryProblem> expected = List.of(
                new LibraryProblem(ProblemType.AVAILABLE_BUT_BORROWED, 7, List.of(2)),
                new LibraryProblem(ProblemType.UNAVAILABLE_NOT_BORROWED, 12, List.of()),
                new LibraryProblem(ProblemType.DUPLICATE, 33, List.of(2, 9)),
                new LibraryProblem(ProblemType.AVAILABLE_BUT_BORROWED, 33, List.of(2, 9)),
                new LibraryProblem(ProblemType.DUPLICATE, 40, List.of(9)),
                new LibraryProblem(ProblemType.MISSING_IN_CATALOG, 55, List.of(2)));
        expect("validateLibrary целиком", expected, new Manager(library).validateLibrary());
        expect("validateLibrary без проблем", List.of(), new Manager(new Library()).validateLibrary());

        System.out.println("ALL LibraryChecks6 PASSED");
    }

    private static Book book(Library library, String name, int id, int year, boolean available) {
        Book book = new Book(name, id, year);
        book.setAvailable(available);
        library.addBook(book);
        return book;
    }

    private static List<Integer> ids(List<Book> books) {
        return books.stream().map(Book::getId).toList();
    }

    private static void expect(String name, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + "\n  ожидали: " + expected + "\n  получили: " + actual);
        }
        System.out.println("PASS " + name);
    }
}
