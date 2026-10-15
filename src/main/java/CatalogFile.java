import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Каталог Library в файле CSV и отчёт о проблемах в файл.
 */
public class CatalogFile {
    public static ImportResult load(Path path, Library library) throws IOException {
        boolean exists = Files.exists(path);
        boolean isDir = Files.isDirectory(path);
        boolean isFile = Files.isRegularFile(path);
        boolean readable = Files.isReadable(path);
        if (!(exists && isDir && isFile && readable)) {
            throw new IOException("Файл отсутствует, либо поврежден.");
        }

        List<Integer> importedIds = new ArrayList<>();
        List<ImportError> errors = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber+=1;
                String[] fields = line.split(";", -1);
                //id;
                if (fields[0] == null || fields[0].isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }
                try {
                    Integer.parseInt(fields[0]);
                } catch (NumberFormatException e) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }

                int bookId = Integer.parseInt(fields[0]);

                if (bookId<0){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }
                if (library.getBooks().containsKey(bookId)){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.DUPLICATE_ID));
                    continue;
                }
                // name;
                if (fields[1] == null || fields[1].isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.EMPTY_NAME));
                    continue;
                }
                // year;
                if (fields[2] == null || fields[2].isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_YEAR));
                    continue;
                }
                try {
                    Integer.parseInt(fields[2]);
                } catch (NumberFormatException e) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }
                if (Integer.parseInt(fields[2])<0){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_ID));
                    continue;
                }
                // available
                if (fields[3] == null || fields[3].isBlank()) {
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_AVAILABLE));
                    continue;
                }
                boolean isBoolean = "true".equalsIgnoreCase(fields[3])
                        || "false".equalsIgnoreCase(fields[3]);
                if (!isBoolean){
                    errors.add(new ImportError(lineNumber, ImportError.Reason.BAD_AVAILABLE));
                    continue;
                }

                //добавление
                library.addBook(new Book(fields[1],bookId,Integer.parseInt(fields[2])));
                library.getBooks().get(bookId).setAvailable(Boolean.parseBoolean(fields[3]));
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
        // TODO: подготовь строки и Files.write(path, lines, StandardCharsets.UTF_8).
        throw new UnsupportedOperationException("TODO: реализуй writeReport");
    }
}
