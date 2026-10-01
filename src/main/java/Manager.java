import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Manager {

    //Стоит ли делать поле final?
    private Library library;
    private int id = 0;

    public Manager(Library library) {
        this.library = library;
    }

    public int generateId() {
        return id++;
    }

    public void addBook(String bookName, int publishYear) {
        int bookId = generateId();

        Book newBook = new Book(bookName, bookId, publishYear);

        library.addBook(newBook);
    }

    public void removeBook(Book bookToRemove) {
        library.removeBook(bookToRemove);
    }

    public Book getBookById(int bookId) {
        return library.getBookById(bookId);
    }

    public Book getBookByName(String bookName) {
        return library.getBookByName(bookName);
    }

    public void returnBook(int bookId,int readerId) {
       library.returnBook(bookId,readerId);
    }

    public void borrowBook(int bookId,int readerId) {
        library.borrowBook(bookId,readerId);
    }

    public List<Book> getAvailableBooks() {
        return library.getAvailableBooks();
    }

    public List<Integer> getAvailableBooksId() {
        return library.getAvailableBooksId();
    }

    public List<Integer> getBorrowedBooksId(){return library.getBorrowedBooksId();}

    public List<Book> getBorrowedBooks() {
        return library.getBorrowedBooks();
    }

    public List<Book> getAllBooks() {
        return library.getAllBooks();
    }

    public Reader getReaderById(int readerId) {
        return library.getReaderById(readerId);
    }

    public List<Reader> getAllReaders() {
        return library.getAllReaders();
    }

    public void addBooks(List<Book> books) {
        library.addBooks(books);
    }

    //выдача нескольких книг читателю
    public OperationResult giveSeveralBooks(int readerId, List<Integer> booksId) {
        if (booksId == null || booksId.isEmpty()) {
            throw new IllegalArgumentException("-Список книжек не должен быть пустым");
        }

        boolean isSuccessful = true;
        String resultMessage = "";

        if (!library.getReaders().containsKey(readerId)){
            isSuccessful = false;
            resultMessage += "-Читатель с таким id не найден\n";
        }else{
            resultMessage += "-Читатель найден\n";
        }

        //id которые существуют -
        List<Integer> presentedIds = booksId.stream()
                .filter(library.getBooks()::containsKey)
                .toList();
        //Eсли мы убираем из списка booksId те которые существуют,
        //и при этом он пуст, то все id существуют. если есть остаток -
        //это не существующие id книг
        List<Integer> unpresentedIds = new ArrayList<>(booksId);
        unpresentedIds.removeAll(presentedIds);
        if (!unpresentedIds.isEmpty()){
            isSuccessful = false;
            resultMessage += "-Книги с этими id отсутствуют в библиотеке -" + unpresentedIds + "\n";
        }else{
            resultMessage += "-Все книги в библиотеке существуют\n";
        }

        //проверка на доступность
        //почему идея хочет обернуть это условие в хэшсет?
        if (!library.getAvailableBooksId().containsAll(booksId)){
            isSuccessful = false;
            resultMessage += "-В запросе содержатся недоступные книги\n";
        }else {
            resultMessage += "- в библиотеке все книги доступны к выдаче\n";
        }

        //проверка на дубликаты
        Set<Integer> unique = new HashSet<>(booksId);
        if(unique.size()!=booksId.size()){
            isSuccessful = false;
            resultMessage += "-В запросе есть дубликаты.\n";
        }else {
            resultMessage += "- В запросе нет дубликатов\n";
        }

        //проверка, что количество книг у читателя после выдачи не больше 5,
        // перед ней проверка на null чтобы программа не упала
        Reader reader = library.getReaders().get(readerId);
        if (reader != null) {
            List<Book> readerBooks = reader.getBooks();

            if (readerBooks.size() + booksId.size() > 5) {
                isSuccessful = false;
                resultMessage += "-Количество книг читателя превышает 5\n";
            } else {
                resultMessage += "-Количество книг после выдачи не превысит 5\n";
            }
        }

        //случай успеха
        if (isSuccessful){
            for (Integer readersId : booksId) {
                library.borrowBook(readersId, readerId);
            }
            resultMessage += "-Книги успешно выданы, приятного чтения\n";
        } else {
            resultMessage += "-Попробуйте учесть причины отказа и обновить данные\n";
        }

        //перечисление признаков успеха и причин отказа-

        return new OperationResult(isSuccessful, resultMessage);

    }

    //Статистика выданных книг
    public Map<Integer, Map<String, BorrowedBookStat>> getBorrowedStats() {

        Map<Integer, Map<String, BorrowedBookStat>> result = new TreeMap<>();

        for (Reader reader : library.getReaders().values()) {
            for (Book book : reader.getBooks()) {

                int decade = (book.getPublishYear() / 10) * 10;

                result.putIfAbsent(decade, new TreeMap<>());

                Map<String, BorrowedBookStat> books = result.get(decade);

                BorrowedBookStat stat = books.get(book.getBookName());

                if (stat == null) {
                    stat = new BorrowedBookStat(
                            0,
                            new ArrayList<>()
                    );
                    books.put(book.getBookName(), stat);
                }

                stat.setCount(stat.getCount() + 1);

                if (!stat.getReaderNames().contains(reader.getName())) {
                    stat.getReaderNames().add(reader.getName());
                }
            }
        }

        for (Map<String, BorrowedBookStat> books : result.values()) {
            for (BorrowedBookStat stat : books.values()) {
                stat.getReaderNames().sort(String::compareTo);
            }
        }

        return result;
    }

    public List<LibraryProblem> validateLibrary() {
        List<LibraryProblem> result = new ArrayList<>();

        List<Integer> borrowedBooks = getAllReaders().stream()
                .flatMap(reader -> reader.getBooks().stream())
                .map(Book::getId)
                .toList();
        List<Integer> availableBooks = getAvailableBooksId();
        //id книги встречается несколько раз

        boolean hasDuplicates = borrowedBooks.size() != new HashSet<>(borrowedBooks).size();
        if (hasDuplicates) {
            Set<Integer> dupBookIds = borrowedBooks.stream()
                    .filter(n -> Collections.frequency(borrowedBooks, n) > 1)
                    .collect(Collectors.toSet());

            Set<Integer> dupReaderIds = getAllReaders().stream()
                    .filter(reader -> reader.getBooks().stream()
                            .anyMatch(book -> dupBookIds.contains(book.getId())))
                    .map(Reader::getId)
                    .collect(Collectors.toSet());
            result.add(new LibraryProblem("Найдены дубли в списках выдачи", dupBookIds, dupReaderIds));
        }

        //книга находится у читателя, но помечена доступной
        Set<Integer> booksInLists = getAllReaders().stream()
                .flatMap(reader -> reader.getBooks().stream())
                .map(Book :: getId)
                .filter(availableBooks::contains)
                .collect(Collectors.toSet());
        if (!booksInLists.isEmpty()){
            Set<Integer> readerIds = getAllReaders().stream()
                    .filter(reader -> reader.getBooks().stream()
                            .anyMatch(book -> availableBooks.contains(book.getId())))
                    .map(Reader::getId)
                    .collect(Collectors.toSet());
            result.add(new LibraryProblem("У читателя найдена книга, которая помечена доступной",booksInLists,readerIds));
        }

        //книга недоступна, но ни у одного читателя ее нет
        //тут нужно возвращать пустой список читателей?
        Set<Integer> unavailableBooks = getBorrowedBooksId().stream()
                .filter(id -> getAllReaders().stream()
                        .flatMap(reader -> reader.getBooks().stream())
                        .noneMatch(book -> book.getId() == id))
                .collect(Collectors.toSet());
        if (!unavailableBooks.isEmpty()){
            Set<Integer> emptyReadersSet = new HashSet<>();
            result.add(new LibraryProblem("Книга недоступна, но ни у одного читателя ее нет", unavailableBooks, emptyReadersSet ));
        }

        //у читателя есть книга, отсутствующая в каталоге
        Set<Integer> allBookIds = getAllBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toSet());

        getAllReaders().stream()
                .flatMap(reader -> reader.getBooks().stream()
                        .filter(book -> !allBookIds.contains(book.getId()))
                        .map(book -> new LibraryProblem(
                                "У читателя есть книга, отсутствующая в каталоге",
                                Set.of(book.getId()),
                                Set.of(reader.getId())
                        )))
                .forEach(result::add);

        return result;
    }

    public SearchResult search(
            Optional<String> namePart,
            Optional<Integer> minYear,
            Optional<Integer> maxYear,
            Optional<Boolean> isAvailable,
            int page,
            int pageSize) {

        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Некорректный номер или размер страницы");
        }

        if (minYear != null && maxYear != null
                && minYear.isPresent() && maxYear.isPresent()
                && minYear.get() > maxYear.get()) {
            throw new IllegalArgumentException("Минимальный год больше максимального");
        }

        List<Book> resultList = new ArrayList<>(getAllBooks());

        if (namePart != null && namePart.isPresent()) {
            String name = namePart.get().trim().toLowerCase();

            if (!name.isEmpty()) {
                resultList = resultList.stream()
                        .filter(book -> book.getBookName()
                                .toLowerCase()
                                .contains(name))
                        .toList();
            }
        }

        if (minYear != null && minYear.isPresent()) {
            resultList = resultList.stream()
                    .filter(book -> book.getPublishYear() >= minYear.get())
                    .toList();
        }

        if (maxYear != null && maxYear.isPresent()) {
            resultList = resultList.stream()
                    .filter(book -> book.getPublishYear() <= maxYear.get())
                    .toList();
        }

        if (isAvailable != null && isAvailable.isPresent()) {
            boolean available = isAvailable.get();

            resultList = resultList.stream()
                    .filter(book -> book.isAvailable() == available)
                    .toList();
        }

        resultList = new ArrayList<>(resultList);

        resultList.sort(
                Comparator.comparingInt(Book::getPublishYear)
                        .reversed()
                        .thenComparing(Book::getBookName)
                        .thenComparingInt(Book::getId)
        );

        int total = resultList.size();

        int from = page * pageSize;

        if (from >= total) {
            return new SearchResult(new ArrayList<>(), total);
        }

        int to = Math.min(from + pageSize, total);

        List<BookView> books = resultList.subList(from, to).stream()
                .map(book -> new BookView(
                        book.getPublishYear(),
                        book.getBookName(),
                        book.getId(),
                        book.isAvailable()
                ))
                .toList();

        return new SearchResult(books, total);
    }
}
