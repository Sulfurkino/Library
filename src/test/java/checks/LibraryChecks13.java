package checks;

import model.Book;
import model.Library;
import model.Reader;
import service.Manager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Проверки занятия 13: срок возврата книги и просроченные книги.
 * Положи файл в src/test/java и запусти main зелёной стрелкой в IDEA.
 * В конце должна быть строка «OK 6 из 6». Этот файл не меняй.
 */
public class LibraryChecks13 {
    private static int passed = 0;
    private static int total = 0;

    private static final LocalDate NOV_1 = LocalDate.of(2026, 11, 1);

    public static void main(String[] args) {
        check("выдали 1 ноября — вернуть до 15 ноября", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2, NOV_1);
            return expect(LocalDate.of(2026, 11, 15), f.library.getBookById(7).getDueDate());
        });

        check("неудачная выдача срок не меняет", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2, NOV_1);
            f.library.borrowBook(7, 9, LocalDate.of(2026, 11, 5));
            return expect(LocalDate.of(2026, 11, 15), f.library.getBookById(7).getDueDate());
        });

        check("после возврата срока нет", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2, NOV_1);
            f.library.returnBook(7, 2);
            Book book = f.library.getBookById(7);
            return book.getDueDate() == null ? null : "ждали null, получили " + book.getDueDate();
        });

        check("в день срока книга не просрочена, на следующий — просрочена", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2, NOV_1);
            String onDueDay = expect(List.of(), f.manager().getOverdueBookIds(LocalDate.of(2026, 11, 15)));
            return onDueDay != null ? "15 ноября: " + onDueDay : expect(List.of(7), f.manager().getOverdueBookIds(LocalDate.of(2026, 11, 16)));
        });

        check("просроченные по сроку возврата: раньше срок — раньше в списке", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2, NOV_1);
            f.library.borrowBook(33, 9, LocalDate.of(2026, 10, 20));
            return expect(List.of(33, 7), f.manager().getOverdueBookIds(LocalDate.of(2026, 11, 20)));
        });

        check("возвращённая книга не просрочена", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(12, 2, LocalDate.of(2026, 10, 1));
            f.library.returnBook(12, 2);
            return expect(List.of(), f.manager().getOverdueBookIds(LocalDate.of(2026, 11, 20)));
        });

        System.out.println();
        System.out.println("OK " + passed + " из " + total);
    }

    /** Каталог из книг 7, 12, 33 и читатели 2 и 9 с пустыми руками. */
    private static class Fixture {
        final Library library = new Library();

        Fixture() {
            library.addBook(new Book("Семь", 7, 2020));
            library.addBook(new Book("Двенадцать", 12, 2021));
            library.addBook(new Book("Тридцать три", 33, 2022));
            Map<Integer, Reader> readers = new HashMap<>();
            readers.put(2, new Reader(2, "Второй"));
            readers.put(9, new Reader(9, "Девятый"));
            library.setReaders(readers);
        }

        Manager manager() {
            return new Manager(library);
        }
    }

    private interface Check {
        String run();
    }

    private static String expect(Object expected, Object actual) {
        return expected.equals(actual) ? null : "ждали " + expected + ", получили " + actual;
    }

    private static void check(String name, Check body) {
        total++;
        String problem;
        try {
            problem = body.run();
        } catch (RuntimeException e) {
            problem = "исключение " + e;
        }
        if (problem == null) {
            passed++;
            System.out.println("OK   " + name);
        } else {
            System.out.println("FAIL " + name + ": " + problem);
        }
    }
}
