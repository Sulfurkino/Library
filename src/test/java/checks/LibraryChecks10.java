package checks;

import model.Book;
import model.Library;
import model.Reader;
import service.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Проверки шага 1 ДЗ после занятия 10: история читателя и карта «кто читал книгу».
 * Положи файл в src/test/java и запусти main зелёной стрелкой в IDEA.
 * В конце должна быть строка «OK 6 из 6». Этот файл не меняй.
 */
public class LibraryChecks10 {
    private static int passed = 0;
    private static int total = 0;

    public static void main(String[] args) {
        check("новый читатель начинает с пустой истории", () -> {
            Reader reader = new Reader( 2, "Второй");
            return expect(List.of(), reader.getHistory());
        });

        check("borrowBook кладёт id книги в историю", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            return expect(List.of(7), f.reader2.getHistory());
        });

        check("returnBook историю не стирает", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            f.library.returnBook(7, 2);
            String books = expect(List.of(), f.reader2.getBooks().stream().map(Book::getId).toList());
            return books != null ? "книги на руках: " + books : expect(List.of(7), f.reader2.getHistory());
        });

        check("неудачная выдача в историю не попадает", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            f.library.borrowBook(7, 9);
            return expect(List.of(), f.reader9.getHistory());
        });

        check("книгу 7 читали 2 и 9, каждый один раз", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            f.library.returnBook(7, 2);
            f.library.borrowBook(7, 2);
            f.library.returnBook(7, 2);
            f.library.borrowBook(7, 9);
            f.library.borrowBook(33, 9);
            Map<Integer, List<Integer>> history = new Manager(f.library).getBookReadersHistory();
            List<Integer> book7 = history.get(7);
            if (book7 == null) {
                return "по книге 7 в карте ничего нет";
            }
            String seven = expect(List.of(2, 9), book7.stream().sorted().toList());
            return seven != null ? "книга 7: " + seven : expect(List.of(9), history.get(33));
        });

        check("книги, которую никто не брал, в карте нет", () -> {
            Fixture f = new Fixture();
            f.library.borrowBook(7, 2);
            Map<Integer, List<Integer>> history = new Manager(f.library).getBookReadersHistory();
            return history.containsKey(12) ? "в карте есть книга 12, её никто не брал" : null;
        });

        System.out.println();
        System.out.println("OK " + passed + " из " + total);
    }

    /** Каталог из книг 7, 12, 33 и читатели 2 и 9 с пустыми руками. */
    private static class Fixture {
        final Library library = new Library();
        final Reader reader2 = new Reader( 2, "Второй");
        final Reader reader9 = new Reader(9, "Девятый");

        Fixture() {
            library.addBook(new Book("Семь", 7, 2020));
            library.addBook(new Book("Двенадцать", 12, 2021));
            library.addBook(new Book("Тридцать три", 33, 2022));
            Map<Integer, Reader> readers = new HashMap<>();
            readers.put(2, reader2);
            readers.put(9, reader9);
            library.setReaders(readers);
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
