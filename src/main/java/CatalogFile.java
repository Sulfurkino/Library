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
                String[] fields = line.split(";", -1);

                lineNumber+=1;
                if (fields.length != 4 ){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.FIELD_COUNT));
                    continue;
                }
                if (line.isEmpty()){
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
        if (Files.isDirectory(path)) {
            throw new IOException("Указанный путь ведёт к директории, а не к файлу.");
        }
        List<LibraryProblem> sortedProblems = problems.stream()
                .sorted(Comparator.comparingInt(LibraryProblem::bookId)
                        .thenComparing(p -> p.type().name()))
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), StandardCharsets.UTF_8))) {
            //добавил после просмотра тестов заголовок чтобы тест на 4 строчки проходил успешно,
            // не понял по тз что нужно сначала писать в репорте именно это,
            // так как формулировка как в тренажере 04 для меня была не ясна
            writer.write("bookId;type;title;readers");
            writer.newLine();

            for (LibraryProblem libraryProblem : sortedProblems) {
                String readers = libraryProblem.readerIds().stream()
                        .sorted()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                writer.write(
                        libraryProblem.bookId() + ";"
                                + libraryProblem.type().name() + ";"
                                + libraryProblem.type().title() + ";"
                                + readers
                );
                writer.newLine();
            }
        }
    }
}
