/** Ошибка импорта: номер строки файла (с 1, считая заголовок) и причина. */
public record ImportError(int lineNumber, Reason reason) {
    public enum Reason { FIELD_COUNT, BAD_ID, EMPTY_NAME, BAD_YEAR, BAD_AVAILABLE, DUPLICATE_ID }
}
