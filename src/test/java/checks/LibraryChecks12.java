package checks;

import model.Book;
import model.Library;
import model.Reader;
import service.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Проверки шага 1 ДЗ после занятия 12: названия книг из истории читателя.
 * Положи файл в src/test/java и запусти main зелёной стрелкой в IDEA.
 * В конце должна быть строка «OK 6 из 6». Этот файл не меняй.
 */
public class LibraryChecks12 {
    private static int passed = 0;
    private static int total = 0;

    public static void main(String[] args) {
        check("читатель без истории — пустой список", () -> {
            Fixture f = new Fixture();
            return expect(List.of(), f.manager().getReaderHistoryBooks(2));
        });

        check("читателя нет в библиотеке — пустой список, без исключения", () -> {
            Fixture f = new Fixture();
            return expect(List.of(), f.manager().getReaderHistoryBooks(99));
        });

        check("названия по порядку выдачи, а не по алфавиту и не по id", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(33, 2);
            f.library.returnBook(33, 2);
            f.library.borrowBook(7, 2);
            return expect(List.of("Тридцать три", "Семь"), f.manager().getReaderHistoryBooks(2));
        });

        check("брал книгу дважды — в списке она дважды", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            f.library.returnBook(7, 2);
            f.library.borrowBook(7, 2);
            return expect(List.of("Семь", "Семь"), f.manager().getReaderHistoryBooks(2));
        });

        check("книгу убрали из каталога — её в списке нет", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(12, 2);
            f.library.returnBook(12, 2);
            f.library.borrowBook(7, 2);
            f.library.removeBook(f.library.getBookById(12));
            return expect(List.of("Семь"), f.manager().getReaderHistoryBooks(2));
        });

        check("чужая история в список не попадает", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            f.library.borrowBook(33, 9);
            return expect(List.of("Тридцать три"), f.manager().getReaderHistoryBooks(9));
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
            readers.put(2, new Reader( 2, "Второй"));
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
