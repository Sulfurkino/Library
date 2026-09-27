import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        Library library = new Library();
        Manager manager = new Manager(library);

        // =========================
        // КНИГИ
        // =========================

        Book book1 = new Book("Java", 1, 2020);
        Book book2 = new Book("Java", 2, 2020);
        Book book3 = new Book("Spring", 3, 2022);
        Book book4 = new Book("Spring Boot", 4, 2024);
        Book book5 = new Book("Algorithms", 5, 2019);
        Book book6 = new Book("Clean Code", 6, 2008);
        Book book7 = new Book("Docker", 7, 2023);

        library.addBooks(List.of(
                book1,
                book2,
                book3,
                book4,
                book5,
                book6,
                book7
        ));

        // =========================
        // ЧИТАТЕЛИ
        // =========================

        Reader reader1 = new Reader(
                new ArrayList<>(),
                1,
                "Иван"
        );

        Reader reader2 = new Reader(
                new ArrayList<>(),
                2,
                "Анна"
        );

        library.getReaders().put(reader1.getId(), reader1);
        library.getReaders().put(reader2.getId(), reader2);


        // =========================
        // 1. ВЫДАЧА НЕСКОЛЬКИХ КНИГ
        // =========================

        System.out.println("=== 1. Выдача книг ===");

        OperationResult giveResult =
                manager.giveSeveralBooks(1, List.of(1, 3));

        System.out.println(giveResult);


        // =========================
        // 2. СТАТИСТИКА
        // =========================

        System.out.println("\n=== 2. Статистика ===");

        System.out.println(manager.getBorrowedStats());


        // =========================
        // 3. ПОИСК ПО НАЗВАНИЮ
        // =========================

        System.out.println("\n=== 3. Поиск по названию ===");

        SearchResult nameResult = manager.search(
                Optional.of("  JAVA  "),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                0,
                10
        );

        System.out.println(nameResult);


        // =========================
        // 4. ПОИСК ПО ДИАПАЗОНУ ГОДОВ
        // =========================

        System.out.println("\n=== 4. Поиск по годам ===");

        SearchResult yearResult = manager.search(
                Optional.empty(),
                Optional.of(2020),
                Optional.of(2023),
                Optional.empty(),
                0,
                10
        );

        System.out.println(yearResult);


        // =========================
        // 5. ПОИСК НЕДОСТУПНЫХ КНИГ
        // =========================

        System.out.println("\n=== 5. Недоступные книги ===");

        SearchResult unavailableResult = manager.search(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(false),
                0,
                10
        );

        System.out.println(unavailableResult);


        // =========================
        // 6. ПАГИНАЦИЯ
        // =========================

        System.out.println("\n=== 6. Пагинация ===");

        SearchResult page0 = manager.search(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                0,
                3
        );

        SearchResult page1 = manager.search(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                1,
                3
        );

        SearchResult page2 = manager.search(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                2,
                3
        );

        SearchResult page3 = manager.search(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                3,
                3
        );

        System.out.println("Страница 0: " + page0);
        System.out.println("Страница 1: " + page1);
        System.out.println("Страница 2: " + page2);
        System.out.println("Страница 3: " + page3);


        // =========================
        // 7. ОШИБКИ ПОИСКА
        // =========================

        System.out.println("\n=== 7. Проверка ошибок ===");

        try {
            manager.search(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    -1,
                    3
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Отрицательная страница: OK");
        }

        try {
            manager.search(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    0,
                    0
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Нулевой размер страницы: OK");
        }

        try {
            manager.search(
                    Optional.empty(),
                    Optional.of(2025),
                    Optional.of(2020),
                    Optional.empty(),
                    0,
                    3
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Обратный диапазон годов: OK");
        }


        // =========================
        // 8. ЛОМАЕМ БИБЛИОТЕКУ
        // =========================

        System.out.println("\n=== 8. Проверка целостности библиотеки ===");

        // Нарушение №1:
        // одна книга находится у двух читателей
        book2.setAvailable(false);

        reader1.getBooks().add(book2);
        reader2.getBooks().add(book2);


        // Нарушение №2:
        // книга недоступна, но ни у одного читателя её нет
        book5.setAvailable(false);


        // Нарушение №3:
        // у читателя есть книга, которой нет в каталоге
        Book ghostBook = new Book(
                "Ghost Book",
                999,
                2025
        );

        reader2.getBooks().add(ghostBook);


        // Запускаем проверку
        List<LibraryProblem> problems =
                manager.validateLibrary();

        System.out.println("Найдено проблем: " + problems.size());

        for (LibraryProblem problem : problems) {
            System.out.println(problem);
        }
    }
}
