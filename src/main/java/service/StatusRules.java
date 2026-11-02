package service;

public class StatusRules {
    //Реализуй find. Он смотрит на каждую книгу каталога books.
    //• Книга помечена доступной, но есть у читателей: проблема AVAILABLE_BUT_BORROWED с id этих читателей.
    //• Книга недоступна, но ни у одного читателя её нет: проблема UNAVAILABLE_NOT_BORROWED без читателей.
    //• Книга у читателя, которой нет в каталоге, сюда не относится: это правило service.TwoRulesTask.
    //• Проблемы верни по возрастанию bookId.
    //
    //Книги сравнивай по id. model.Book, model.Reader, model.LibraryProblem, model.ProblemType и PracticeAdapter не меняй.
//    public static List<model.LibraryProblem> find(List<model.Book> books, List<model.Reader> readers) {
//
//    }
}