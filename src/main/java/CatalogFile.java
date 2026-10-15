import java.io.*;
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
        if (!Files.exists(path)) {
            throw new IOException("Файл не существует.");
        }

        if (Files.isDirectory(path)) {
            throw new IOException("Указанный путь ведёт к директории, а не к файлу.");
        }

        if (!Files.isRegularFile(path)) {
            throw new IOException("Указанный путь не является обычным файлом.");
        }

        if (!Files.isReadable(path)) {
            throw new IOException("Файл недоступен для чтения.");
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
//    public static void writeCSV(String filename, List<String[]> records) throws IOException {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
//            for (String[] record : records) {
//                writer.write(String.join(",", record));
//                writer.newLine();
//            }
//        }
//    }
    public static void writeReport(Path path, List<LibraryProblem> problems) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("Файл не существует.");
        }

        if (Files.isDirectory(path)) {
            throw new IOException("Указанный путь ведёт к директории, а не к файлу.");
        }

        if (!Files.isRegularFile(path)) {
            throw new IOException("Указанный путь не является обычным файлом.");
        }

        if (!Files.isReadable(path)) {
            throw new IOException("Файл недоступен для чтения.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), StandardCharsets.UTF_8))) {
            for (LibraryProblem libraryProblem : problems) {
                writer.write(libraryProblem.toString());
                writer.newLine();
            }
        }
    }
}
