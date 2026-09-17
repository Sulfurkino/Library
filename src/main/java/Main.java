import java.util.ArrayList;
import java.util.Arrays;

//надеюсь ничего страшного, что я заставил чат gpt делать тесты
public class Main {
    public static void main(String[] args) {

        Library library = new Library();

        Manager manager = new Manager(library);

        // Добавляем книги
        manager.addBook("Java для начинающих", 2020); // id 0
        manager.addBook("Effective Java", 2018);      // id 1
        manager.addBook("Clean Code", 2008);           // id 2
        manager.addBook("Head First Java", 2019);      // id 3
        manager.addBook("Spring in Action", 2022);     // id 4
        manager.addBook("Java Concurrency", 2021);     // id 5
        manager.addBook("Design Patterns", 1994);      // id 6


        Reader reader1 = new Reader(
                new ArrayList<>(),
                1,
                "Анна"
        );

        Reader reader2 = new Reader(
                new ArrayList<>(),
                2,
                "Иван"
        );

        // Добавляем читателей в библиотеку
        library.getReaders().put(reader1.getId(), reader1);
        library.getReaders().put(reader2.getId(), reader2);


        // ==========================
        // ПРОВЕРКА giveSeveralBooks
        // ==========================

        // 1. Успешная выдача двух книг Анне
        System.out.println("=== Тест 1 ===");
        manager.giveSeveralBooks(
                1,
                Arrays.asList(0, 1)
        );

        // 2. Попытка выдать несуществующую книгу
        System.out.println("\n=== Тест 2 ===");
        manager.giveSeveralBooks(
                1,
                Arrays.asList(2, 999)
        );

        // 3. Попытка выдать уже занятую книгу
        System.out.println("\n=== Тест 3 ===");
        manager.giveSeveralBooks(
                2,
                Arrays.asList(0, 3)
        );

        // 4. Дубликат ID
        System.out.println("\n=== Тест 4 ===");
        manager.giveSeveralBooks(
                2,
                Arrays.asList(2, 2, 3)
        );

        // 5. Несуществующий читатель
        System.out.println("\n=== Тест 5 ===");
        manager.giveSeveralBooks(
                999,
                Arrays.asList(4, 5)
        );

        // 6. Выдать несколько книг Ивану
        System.out.println("\n=== Тест 6 ===");
        manager.giveSeveralBooks(
                2,
                Arrays.asList(4, 5, 6)
        );

        // 7. Проверяем состояние библиотеки
        System.out.println("\n=== Состояние ===");

        System.out.println("Книги Анны: " +
                reader1.getBooks());

        System.out.println("Книги Ивана: " +
                reader2.getBooks());

        System.out.println("Свободные книги: " +
                manager.getAvailableBooks());

        System.out.println("Все книги: " +
                manager.getAllBooks());
    }

}
