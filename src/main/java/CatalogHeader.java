import java.util.List;
import java.util.Map;
import java.util.HashMap;
/** Часть 02/06. Заголовок файла каталога: какая колонка на каком месте. */
public class CatalogHeader {
    /** Колонки, которые должны быть в заголовке, каждая ровно один раз. */
    public static final List<String> REQUIRED = List.of("id", "name", "year", "available");

    public static Map<String, Integer> columns(String header) {
        if (header == null || header.isBlank()) {
            throw new IllegalArgumentException("Заголовок пустой, или не существует.");
        }

        String[] fields = header.split(";", -1);
        Map<String, Integer> result = new HashMap<>();

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();

            if (!REQUIRED.contains(field)) {
                throw new IllegalArgumentException(
                        "Название колонки не соответствует заданным параметрам. - " + field + " " + i
                );
            }

            if (result.put(field, i) != null) {
                throw new IllegalArgumentException(
                        "Колонка " + field + " под номером " + i + " - повторяется."
                );
            }
        }

        for (String field : REQUIRED) {
            if (!result.containsKey(field)) {
                throw new IllegalArgumentException(
                        "В заголовке отсутствует ключевое слово: " + field
                );
            }
        }

        return result;
    }
}