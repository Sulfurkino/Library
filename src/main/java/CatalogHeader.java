import java.util.List;
import java.util.Map;
import java.util.HashMap;
/** Часть 02/06. Заголовок файла каталога: какая колонка на каком месте. */
public class CatalogHeader {
    /** Колонки, которые должны быть в заголовке, каждая ровно один раз. */
    public static final List<String> REQUIRED = List.of("id", "name", "year", "available");

    /**
     * header — первая строка файла, например "id;name;year;available".
     * Верни, на каком месте стоит каждая колонка: {id=0, name=1, year=2, available=3}.
     * Колонки могут идти в любом порядке, пробелы вокруг имён обрежь.
     * Если колонки не хватает, она повторяется или есть лишняя, брось IllegalArgumentException
     * и напиши в сообщении, какая колонка и что с ней не так.
     */
    public static Map<String, Integer> columns(String header) {
        if(header == null||header.isBlank()){
            throw new IllegalArgumentException("Заголовок пустой, или не существует.");
        }
        String[] fields =  header.split(";", -1);
        Map<String,Integer> result = new HashMap<>();
        if(fields.length != 4){
            throw new IllegalArgumentException("Количество колонок не соответствует четырем.");
        }
        for(int i = 0; i < fields.length; i ++){
            String field = fields[i].trim();
            if(! REQUIRED.contains(field)){
                throw new IllegalArgumentException("Название колонки не соответсвует заданным параметрам. - " + field + " " + i );
            }
            if(result.put(field,i) != null){
                throw new IllegalArgumentException("Колонка под номером " + i + " - повторяется.");
            }

        }
        for(String field : REQUIRED){
            if(!result.containsKey(field)){
                throw new IllegalArgumentException("В заголовке отсутствует ключевое слово.");
            }
        }
        return result;
    }
}