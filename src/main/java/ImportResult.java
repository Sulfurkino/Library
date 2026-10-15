import java.util.List;

/** Результат импорта: ID принятых книг и ошибки. Списки копируются и не меняются. */
public record ImportResult(List<Integer> importedIds, List<ImportError> errors) {
    public ImportResult {
        importedIds = List.copyOf(importedIds);
        errors = List.copyOf(errors);
    }
}
