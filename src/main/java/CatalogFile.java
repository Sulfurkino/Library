import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Каталог Library в файле CSV и отчёт о проблемах в файл.
 */
public class CatalogFile {
    public static ImportResult load(Path path, Library library) throws IOException {
        List<Integer> importedIds = new ArrayList<>();
        List<ImportError> errors = new ArrayList<>();

        //fields[columns.get("year")]
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            if (line == null){
                throw new IllegalArgumentException("Первая строка пуста.");
            }
            Map<String, Integer> columns = CatalogHeader.columns(line);
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = line.split(";", -1);

                if (fields.length != 4) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.FIELD_COUNT));
                    continue;
                }
                //id;
                if (fields[columns.get("id")].trim().isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }
                int bookId;
                try {
                    bookId = Integer.parseInt(fields[columns.get("id")].trim());
                } catch (NumberFormatException e) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }

                if (bookId<0){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }
                // name;
                if (fields[columns.get("name")].trim().isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.EMPTY_NAME));
                    continue;
                }
                // year;
                if (fields[columns.get("year")].trim().isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_YEAR));
                    continue;
                }
                try {
                    Integer.parseInt(fields[columns.get("year")].trim());
                } catch (NumberFormatException e) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_YEAR));
                    continue;
                }

                // available
                if (fields[columns.get("available")].trim().isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_AVAILABLE));
                    continue;
                }
                boolean isBoolean = "true".equalsIgnoreCase(fields[columns.get("available")].trim())
                        || "false".equalsIgnoreCase(fields[columns.get("available")].trim());
                if (!isBoolean){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_AVAILABLE));
                    continue;
                }

                if (library.getBooks().containsKey(bookId)){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.DUPLICATE_ID));
                    continue;
                }

                //добавление
                library.addBook(new Book(fields[columns.get("name")].trim(),bookId,Integer.parseInt(fields[columns.get("year")].trim())));
                library.getBooks().get(bookId).setAvailable(Boolean.parseBoolean(fields[columns.get("available")].trim()));
                importedIds.add(bookId);
            }

        }
        return new ImportResult(importedIds, errors);
    }

    /**
     * Пишет отчёт (UTF-8): заголовок bookId;type;title;readers и строки как в тренажёре 04.
     * Тип — name(), не ordinal(). Существующий файл перезаписывается.
     */

    public static void writeReport(Path path, List<LibraryProblem> problems) throws IOException {
        List<LibraryProblem> problemsCopy = problems.stream()
                .sorted(ReportOrder.byBookAndType())
                .toList();
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8) ){
            bufferedWriter.write("bookId;type;title;readers");
            bufferedWriter.newLine();
            for (LibraryProblem problem : problemsCopy){
                String line = ReportLine.toLine(problem);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }



        }
    }
    public static List<LibraryProblem> readReport(Path path){
        throw new UnsupportedOperationException();
    }
}
