import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Каталог Library в файле CSV и отчёт о проблемах в файл.
 */
public class CatalogFile {
    public static ImportResult load(Path path, Library library) throws IOException {
        List<Integer> importedIds = new ArrayList<>();
        List<ImportError> errors = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Первая строка пуста.");
            }

            Map<String, Integer> columns = CatalogHeader.columns(line);
            int lineNumber = 1;

            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(";", -1);

                if (fields.length != columns.size()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.FIELD_COUNT));
                    continue;
                }

                String idText = fields[columns.get("id")].trim();
                String nameText = fields[columns.get("name")].trim();
                String yearText = fields[columns.get("year")].trim();
                String availableText = fields[columns.get("available")].trim();

                // id
                int bookId;
                try {
                    bookId = Integer.parseInt(idText);
                } catch (NumberFormatException e) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }

                if (bookId < 0) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }

                // name
                if (nameText.isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.EMPTY_NAME));
                    continue;
                }

                // year
                int year;
                try {
                    year = Integer.parseInt(yearText);
                } catch (NumberFormatException e) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_YEAR));
                    continue;
                }

                // available
                boolean isBoolean = "true".equalsIgnoreCase(availableText)
                        || "false".equalsIgnoreCase(availableText);
                if (!isBoolean) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_AVAILABLE));
                    continue;
                }

                if (library.getBooks().containsKey(bookId)) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.DUPLICATE_ID));
                    continue;
                }

                // добавление
                library.addBook(new Book(nameText, bookId, year));
                library.getBooks().get(bookId).setAvailable(Boolean.parseBoolean(availableText));
                importedIds.add(bookId);
            }
        }

        return new ImportResult(importedIds, errors);
    }
    public static void writeReport(Path path, List<LibraryProblem> problems) throws IOException {
        List<LibraryProblem> problemsCopy = problems.stream()
                .sorted(ReportOrder.byBookAndType())
                .toList();
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
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
