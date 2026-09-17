import java.util.*;

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

    public List<Book> getAllBooks() {
        return library.getAllBooks();
    }


    //выдача нескольких книг читателю
    public void giveSeveralBooks(int readerId,List<Integer> booksId){
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
            for (Integer integer : booksId) {
                library.borrowBook(integer, readerId);
            }
            resultMessage += "-Книги успешно выданы, приятного чтения\n";
        } else {
            resultMessage += "-Попробуйте учесть причины отказа и обновить данные\n";
        }

        //перечисление признаков успеха и причин отказа-
        System.out.println(resultMessage);

    }


}
