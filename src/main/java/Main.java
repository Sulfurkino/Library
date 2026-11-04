import model.*;
import service.Manager;
import service.ProblemSummary;
import service.TwoRulesTask;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Library library = new Library();
        Book book40 = new Book("40", 40, 2022);
        Book book50 = new Book("Zulu50", 50, 2022);
        Book book60 = new Book("Zulu60", 60, 2022);

        Reader reader20 = new Reader(new ArrayList<>(),2,"02");
        Reader reader09 = new Reader(new ArrayList<>(),9,"09");

        library.setReaders(Map.of(
                2, reader20,
                9, reader09
        ));

        //HW проверка дз -
        TwoRulesTask twoRulesTask = new TwoRulesTask();
        library.setReaders(Map.of(
                2, reader20,
                9, reader09
        ));

        reader09.getBooks().add(book50);
        reader09.getBooks().add(book50);
        reader09.getBooks().add(book40);
        reader09.getBooks().add(book40);

        reader20.getBooks().add(book50);
        reader20.getBooks().add(book50);
        reader20.getBooks().add(book60);

        for (LibraryProblem libraryProblem : twoRulesTask.findProblems(library)){
            System.out.println(libraryProblem);
        }

        //проверка problemSummary
        List<LibraryProblem> problems = twoRulesTask.findProblems(library);

        Map<ProblemType, Integer> expected = new EnumMap<>(ProblemType.class);
        expected.put(ProblemType.DUPLICATE, 2);
        expected.put(ProblemType.MISSING_IN_CATALOG, 3);
        expected.put(ProblemType.AVAILABLE_BUT_BORROWED, 0);
        expected.put(ProblemType.UNAVAILABLE_NOT_BORROWED, 0);

        Map<ProblemType, Integer> actual = ProblemSummary.countByType(problems);

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    "Тест провалился."
                            + "Ожидалось-" + expected + "\n"
                            + "Получено-" + actual
            );
        }

        System.out.println("Тест пройден");
        System.out.println("Результат-" + actual);

        //HomeWork3
        Library hw3 = new Library();
        Book hw3Book40 = new Book("Сорок", 40, 2000);
        hw3Book40.setAvailable(false);
        hw3.addBook(hw3Book40);
        Book hw3Book50 = new Book("Пятьдесят", 50, 2000);
        Reader hw3Reader2 = new Reader(
                new ArrayList<>(List.of(hw3Book50)), 2, "Второй");
        Reader hw3Reader9 = new Reader(new ArrayList<>(
                List.of(hw3Book40, hw3Book40, hw3Book50)),
                9, "Девятый");
        hw3.setReaders(Map.of(2, hw3Reader2, 9, hw3Reader9));

        List<LibraryProblem> expectedProblems = List.of(
                new LibraryProblem(ProblemType.DUPLICATE, 40, List.of(9)),
                new LibraryProblem(ProblemType.DUPLICATE, 50, List.of(2, 9)),
                new LibraryProblem(ProblemType.MISSING_IN_CATALOG, 50, List.of(2, 9))
        );

        List<LibraryProblem> actualProblems = new Manager(hw3).validateLibrary();

        if (!expectedProblems.equals(actualProblems)) {
            throw new AssertionError(
                    "Ошибка. Ожидали - " + expectedProblems + "\n"
                    + "Получили- " + actualProblems
            );
        }

        System.out.println("Успех");
        System.out.println("Результат-");
        for (LibraryProblem libraryProblem : actualProblems) {
            System.out.println(libraryProblem);
        }


//        // ============================================================
//        // 1. Сортировка и страницы
//        // ============================================================
//
//        dto.SearchResult page0 = manager.search(
//                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 2
//        );
//
//        checkIds(page0, List.of(30, 20), "page 0");
//        check(page0.getTotal() == 4, "page 0 total");
//
//        dto.SearchResult page1 = manager.search(
//                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
//                1, 2
//        );
//
//        checkIds(page1, List.of(40, 10), "page 1");
//        check(page1.getTotal() == 4, "page 1 total");
//
//        dto.SearchResult page2 = manager.search(
//                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
//                2, 2
//        );
//
//        checkIds(page2, List.of(), "page 2");
//        check(page2.getTotal() == 4, "page 2 total");
//
//        System.out.println("OK: sorting + pagination");
//
//
//        // ============================================================
//        // 2. Каждый фильтр отдельно
//        // ============================================================
//
//        dto.SearchResult byName = manager.search(
//                Optional.of("Alpha"),
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        checkIds(byName, List.of(20, 40), "name filter");
//
//
//        dto.SearchResult byMinYear = manager.search(
//                Optional.empty(),
//                Optional.of(2021),
//                Optional.empty(),
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(byMinYear, List.of(30), "minYear filter");
//
//
//        dto.SearchResult byMaxYear = manager.search(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.of(2020),
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(byMaxYear, List.of(20, 40, 10), "maxYear filter");
//
//
//        // Все книги доступны.
//        dto.SearchResult available = manager.search(
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                Optional.of(true),
//                0, 10
//        );
//
//        checkIds(available, List.of(30, 20, 40, 10), "availability filter");
//
//        System.out.println("OK: every filter separately");
//
//
//        // ============================================================
//        // 3. Настоящий null у каждого фильтра + другой активный
//        // ============================================================
//
//        dto.SearchResult nameNull = manager.search(
//                null,
//                Optional.of(2021),
//                Optional.empty(),
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(nameNull, List.of(30), "name null + active min");
//
//
//        dto.SearchResult minNull = manager.search(
//                Optional.empty(),
//                null,
//                Optional.of(2020),
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(minNull, List.of(20, 40, 10), "minYear null + active max");
//
//
//        dto.SearchResult maxNull = manager.search(
//                Optional.empty(),
//                Optional.of(2021),
//                null,
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(maxNull, List.of(30), "maxYear null + active min");
//
//
//        dto.SearchResult availabilityNull = manager.search(
//                Optional.of("Alpha"),
//                Optional.empty(),
//                Optional.empty(),
//                null,
//                0, 10
//        );
//
//        checkIds(availabilityNull, List.of(20, 40), "availability null + active name");
//
//
//        // Все четыре null одновременно.
//        dto.SearchResult allNull = manager.search(
//                null, null, null, null,
//                0, 10
//        );
//
//        checkIds(allNull, List.of(30, 20, 40, 10), "all filters null");
//        check(allNull.getTotal() == 4, "all filters null total");
//
//        System.out.println("OK: null filters");
//
//
//        // ============================================================
//        // 4. Optional.empty() отдельно
//        // ============================================================
//
//        dto.SearchResult allEmpty = manager.search(
//                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        checkIds(allEmpty, List.of(30, 20, 40, 10), "all filters Optional.empty");
//
//        System.out.println("OK: Optional.empty()");
//
//
//        // ============================================================
//        // 5. Название: trim + case + empty + blank
//        // ============================================================
//
//        dto.SearchResult alphaWithSpaces = manager.search(
//                Optional.of(" Alpha "),
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        dto.SearchResult alphaNormal = manager.search(
//                Optional.of("alpha"),
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        checkIds(
//                alphaWithSpaces,
//                ids(alphaNormal),
//                "\" Alpha \" == \"alpha\""
//        );
//
//
//        dto.SearchResult emptyName = manager.search(
//                Optional.of(""),
//                Optional.of(2020),
//                Optional.empty(),
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(
//                emptyName,
//                List.of(30, 20, 40, 10),
//                "empty name does not filter"
//        );
//
//
//        dto.SearchResult blankName = manager.search(
//                Optional.of("   "),
//                Optional.of(2020),
//                Optional.empty(),
//                Optional.empty(),
//                0, 10
//        );
//
//        checkIds(
//                blankName,
//                List.of(30, 20, 40, 10),
//                "blank name does not filter"
//        );
//
//        System.out.println("OK: name trim/case/empty/blank");
//
//
//        // ============================================================
//        // 6. Несколько / одно / ноль совпадений
//        // ============================================================
//
//        dto.SearchResult several = manager.search(
//                Optional.of("a"),
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        check(several.getTotal() == 3, "several matches");
//
//
//        dto.SearchResult one = manager.search(
//                Optional.of("zulu"),
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        checkIds(one, List.of(30), "one match");
//        check(one.getTotal() == 1, "one match total");
//
//
//        dto.SearchResult none = manager.search(
//                Optional.of("does-not-exist"),
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10
//        );
//
//        checkIds(none, List.of(), "zero matches");
//        check(none.getTotal() == 0, "zero matches total");
//
//        System.out.println("OK: several/one/zero matches");
//
//
//        // ============================================================
//        // 7. Равные годы и названия
//        // ============================================================
//
//        checkIds(
//                allNull,
//                List.of(30, 20, 40, 10),
//                "equal years/names ordering"
//        );
//
//        // 20 и 40 имеют одинаковые год и название Alpha.
//        // Поэтому используется ID: 20 раньше 40.
//        check(
//                ids(allNull).indexOf(20) < ids(allNull).indexOf(40),
//                "ID tie-breaker"
//        );
//
//        System.out.println("OK: equal years/names ordering");
//
//
//        // ============================================================
//        // 8. Каталог после поиска не изменился
//        // ============================================================
//
//        List<Integer> catalogIds = library.getAllBooks()
//                .stream()
//                .map(model.Book::getId)
//                .sorted()
//                .toList();
//
//        check(
//                catalogIds.equals(List.of(10, 20, 30, 40)),
//                "catalog unchanged"
//        );
//
//        check(
//                library.getAllBooks().size() == 4,
//                "catalog size unchanged"
//        );
//
//        System.out.println("OK: catalog unchanged");
//
//
//        // ============================================================
//        // 9. Итог
//        // ============================================================
//
//        System.out.println();
//        System.out.println("ALL CHECKS PASSED");
//    }
//
//
//    private static List<Integer> ids(dto.SearchResult result) {
//        return result.getBooks()
//                .stream()
//                .map(model.BookView::getId)
//                .toList();
//    }
//
//
//    private static void checkIds(
//            dto.SearchResult actual,
//            List<Integer> expected,
//            String testName) {
//
//        List<Integer> actualIds = ids(actual);
//
//        if (!actualIds.equals(expected)) {
//            throw new AssertionError(
//                    testName
//                            + " FAILED\n"
//                            + "expected: " + expected + "\n"
//                            + "actual:   " + actualIds
//            );
//        }
//
//        System.out.println("OK: " + testName);
//    }
//
//
//    private static void check(
//            boolean condition,
//            String testName) {
//
//        if (!condition) {
//            throw new AssertionError(testName + " FAILED");
//        }
   }
}


//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class Main {
//
//    public static void main(String[] args) {
//
//        model.Library library = new model.Library();
//        service.Manager manager = new service.Manager(library);
//
//        // =========================
//        // КНИГИ
//        // =========================
//
//        model.Book book1 = new model.Book("Java", 1, 2020);
//        model.Book book2 = new model.Book("Java", 2, 2020);
//        model.Book book3 = new model.Book("Spring", 3, 2022);
//        model.Book book4 = new model.Book("Spring Boot", 4, 2024);
//        model.Book book5 = new model.Book("Algorithms", 5, 2019);
//        model.Book book6 = new model.Book("Clean Code", 6, 2008);
//        model.Book book7 = new model.Book("Docker", 7, 2023);
//
//        library.addBooks(List.of(
//                book1,
//                book2,
//                book3,
//                book4,
//                book5,
//                book6,
//                book7
//        ));
//
//        // =========================
//        // ЧИТАТЕЛИ
//        // =========================
//
//        model.Reader reader1 = new model.Reader(
//                new ArrayList<>(),
//                1,
//                "Иван"
//        );
//
//        model.Reader reader2 = new model.Reader(
//                new ArrayList<>(),
//                2,
//                "Анна"
//        );
//
//        library.getReaders().put(reader1.getId(), reader1);
//        library.getReaders().put(reader2.getId(), reader2);
//
//
//        // =========================
//        // 1. ВЫДАЧА НЕСКОЛЬКИХ КНИГ
//        // =========================
//
//        System.out.println("=== 1. Выдача книг ===");
//
//        model.OperationResult giveResult =
//                manager.giveSeveralBooks(1, List.of(1, 3));
//
//        System.out.println(giveResult);
//
//
//        // =========================
//        // 2. СТАТИСТИКА
//        // =========================
//
//        System.out.println("\n=== 2. Статистика ===");
//
//        System.out.println(manager.getBorrowedStats());
//
//
//        // =========================
//        // 3. ПОИСК ПО НАЗВАНИЮ
//        // =========================
//
//        System.out.println("\n=== 3. Поиск по названию ===");
//
//        dto.SearchResult nameResult = manager.search(
//                Optional.of("  JAVA  "),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                0,
//                10
//        );
//
//        System.out.println(nameResult);
//
//
//        // =========================
//        // 4. ПОИСК ПО ДИАПАЗОНУ ГОДОВ
//        // =========================
//
//        System.out.println("\n=== 4. Поиск по годам ===");
//
//        dto.SearchResult yearResult = manager.search(
//                Optional.empty(),
//                Optional.of(2020),
//                Optional.of(2023),
//                Optional.empty(),
//                0,
//                10
//        );
//
//        System.out.println(yearResult);
//
//
//        // =========================
//        // 5. ПОИСК НЕДОСТУПНЫХ КНИГ
//        // =========================
//
//        System.out.println("\n=== 5. Недоступные книги ===");
//
//        dto.SearchResult unavailableResult = manager.search(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.of(false),
//                0,
//                10
//        );
//
//        System.out.println(unavailableResult);
//
//
//        // =========================
//        // 6. ПАГИНАЦИЯ
//        // =========================
//
//        System.out.println("\n=== 6. Пагинация ===");
//
//        dto.SearchResult page0 = manager.search(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                0,
//                3
//        );
//
//        dto.SearchResult page1 = manager.search(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                1,
//                3
//        );
//
//        dto.SearchResult page2 = manager.search(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                2,
//                3
//        );
//
//        dto.SearchResult page3 = manager.search(
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty(),
//                3,
//                3
//        );
//
//        System.out.println("Страница 0: " + page0);
//        System.out.println("Страница 1: " + page1);
//        System.out.println("Страница 2: " + page2);
//        System.out.println("Страница 3: " + page3);
//
//
//        // =========================
//        // 7. ОШИБКИ ПОИСКА
//        // =========================
//
//        System.out.println("\n=== 7. Проверка ошибок ===");
//
//        try {
//            manager.search(
//                    Optional.empty(),
//                    Optional.empty(),
//                    Optional.empty(),
//                    Optional.empty(),
//                    -1,
//                    3
//            );
//        } catch (IllegalArgumentException e) {
//            System.out.println("Отрицательная страница: OK");
//        }
//
//        try {
//            manager.search(
//                    Optional.empty(),
//                    Optional.empty(),
//                    Optional.empty(),
//                    Optional.empty(),
//                    0,
//                    0
//            );
//        } catch (IllegalArgumentException e) {
//            System.out.println("Нулевой размер страницы: OK");
//        }
//
//        try {
//            manager.search(
//                    Optional.empty(),
//                    Optional.of(2025),
//                    Optional.of(2020),
//                    Optional.empty(),
//                    0,
//                    3
//            );
//        } catch (IllegalArgumentException e) {
//            System.out.println("Обратный диапазон годов: OK");
//        }
//
//
//        // =========================
//        // 8. ЛОМАЕМ БИБЛИОТЕКУ
//        // =========================
//
//        System.out.println("\n=== 8. Проверка целостности библиотеки ===");
//
//        // Нарушение №1:
//        // одна книга находится у двух читателей
//        book2.setAvailable(false);
//
//        reader1.getBooks().add(book2);
//        reader2.getBooks().add(book2);
//
//
//        // Нарушение №2:
//        // книга недоступна, но ни у одного читателя её нет
//        book5.setAvailable(false);
//
//
//        // Нарушение №3:
//        // у читателя есть книга, которой нет в каталоге
//        model.Book ghostBook = new model.Book(
//                "Ghost model.Book",
//                999,
//                2025
//        );
//
//        reader2.getBooks().add(ghostBook);
//
//
//        // Запускаем проверку
//        List<model.LibraryProblem> problems =
//                manager.validateLibrary();
//
//        System.out.println("Найдено проблем: " + problems.size());
//
//        for (model.LibraryProblem problem : problems) {
//            System.out.println(problem);
//        }
//    }
//}
