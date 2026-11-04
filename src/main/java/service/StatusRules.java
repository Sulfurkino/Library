package service;

import model.Book;
import model.LibraryProblem;
import model.ProblemType;
import model.Reader;

import java.util.*;
import java.util.stream.Collectors;

public class StatusRules {
    public static List<LibraryProblem> find(List<model.Book> books, List<model.Reader> readers) {
        Map<Integer, List<Integer>> booksAndTheirReaders = readers.stream()
                .flatMap(reader -> reader.getBooks().stream()
                        .map(book -> Map.entry(book.getId(), reader.getId())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue,Collectors.toList())
                ));
//        Map<Integer, List<Integer>> readersAndBooks = new HashMap<>();
//        for(Reader reader : readers){
//            for(Book book : reader.getBooks()){
//                readersAndBooks.computeIfAbsent(book.getId(), integer -> new ArrayList<>()).add(reader.getId());
//            }
//        }
        //Реализуй find. Он смотрит на каждую книгу каталога books.
        //• Книга помечена доступной, но есть у читателей: проблема AVAILABLE_BUT_BORROWED с id этих читателей.
        //• Книга недоступна, но ни у одного читателя её нет: проблема UNAVAILABLE_NOT_BORROWED без читателей.
        //• Книга у читателя, которой нет в каталоге, сюда не относится: это правило service.TwoRulesTask.
        //• Проблемы верни по возрастанию bookId.
        //
        //Книги сравнивай по id. model.Book, model.Reader, model.LibraryProblem, model.ProblemType и PracticeAdapter не меняй.
        List<LibraryProblem> result = new ArrayList<>();
//неудачная попытка -
//        for (Book book : books){
//            for (List<Integer> bookList : booksAndTheirReaders.values()){
//                for (Integer bookId : bookList){
//                    if (book.isAvailable() && book.getId() == bookId ){
//                        result.add(new LibraryProblem(ProblemType.AVAILABLE_BUT_BORROWED,bookId,booksAndTheirReaders.get(bookId)));
//                    }
//                    if (!book.isAvailable() && ){
//                        result.add(new LibraryProblem(ProblemType.UNAVAILABLE_NOT_BORROWED,bookId,booksAndTheirReaders.get(bookId)));
//                    }
//                }
//            }
//
//        }
        for (Book book : books) {
            //если книга есть в Map, получаем список читателей.
            //Если книги нет в Map, получаем пустой список.
            List<Integer> readerIds = booksAndTheirReaders.getOrDefault(
                    book.getId(),
                    List.of()
            );

            if (book.isAvailable()){
                result.add(new LibraryProblem(ProblemType.AVAILABLE_BUT_BORROWED,book.getId(),booksAndTheirReaders.get(book.getId())));
            }

            if (readerIds.isEmpty()) {
                result.add(new LibraryProblem(ProblemType.UNAVAILABLE_NOT_BORROWED,book.getId(),booksAndTheirReaders.get(book.getId())));
            }
        }
        return result.stream()
                .sorted(Comparator.comparing(LibraryProblem::bookId))
                .toList();
    }
}