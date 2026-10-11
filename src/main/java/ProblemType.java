/** Часть 03/06. Тип нарушения в Library: enum вместо строк. */
public enum ProblemType {
    DUPLICATE("Найдены дубли в списках выдачи."),
    MISSING_IN_CATALOG("У читателя есть книга, отсутствующая в каталоге."),
    AVAILABLE_BUT_BORROWED("Книга отмечена доступной, но взята читателем."),
    UNAVAILABLE_NOT_BORROWED("Книга является недоступной, но ее нет ни у одного читателя.");

    private final String title;

    ProblemType(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    /** Константа с точно таким текстом; неизвестный текст — IllegalArgumentException. */
    public static ProblemType fromTitle(String title) {
        // TODO: перебери values() и сравни тексты.
        for(ProblemType problemType : values()){
            if (problemType.title().equals(title)){
                return problemType;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип нарушения.");
    }
}