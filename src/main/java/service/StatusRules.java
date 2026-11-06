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
//        List<LibraryProblem> result = new ArrayList<>();
//        for (Book book : books) {
//            List<Integer> readerIds = booksAndTheirReaders.getOrDefault(
//                    book.getId(),
//                    List.of()
//            );
//            if (book.isAvailable() && !readerIds.isEmpty()){
//                result.add(new LibraryProblem(ProblemType.AVAILABLE_BUT_BORROWED,book.getId(),booksAndTheirReaders.get(book.getId())));
//            }
//            if (!book.isAvailable() && readerIds.isEmpty()) {
//                result.add(new LibraryProblem(ProblemType.UNAVAILABLE_NOT_BORROWED,book.getId(),booksAndTheirReaders.get(book.getId())));
//            }
//        }
//        return result.stream()
//                .sorted(Comparator.comparing(LibraryProblem::bookId))
//                .toList();

        return books.stream()
                .map(book -> Map.entry(
                        book,
                        booksAndTheirReaders.getOrDefault(book.getId(), List.of())
                ))
                .filter(entry -> {
                    Book book = entry.getKey();
                    List<Integer> readerIds = entry.getValue();

                    return (book.isAvailable() && !readerIds.isEmpty())
                            || (!book.isAvailable() && readerIds.isEmpty());
                })
                .map(entry -> {
                    Book book = entry.getKey();
                    List<Integer> readerIds = entry.getValue();

                    ProblemType type = book.isAvailable()
                            ? ProblemType.AVAILABLE_BUT_BORROWED
                            : ProblemType.UNAVAILABLE_NOT_BORROWED;

                    return new LibraryProblem(type, book.getId(), readerIds);
                })
                .sorted(Comparator.comparingInt(LibraryProblem::bookId))
                .toList();
    }

}