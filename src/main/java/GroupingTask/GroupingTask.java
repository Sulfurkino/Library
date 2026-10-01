package GroupingTask;

import java.util.*;

public class GroupingTask {
    static class Loan {
        final int bookId;
        final int readerId;

        Loan(int bookId, int readerId) {
            this.bookId = bookId;
            this.readerId = readerId;
        }
    }

    // Одна строка DUPLICATE для каждой книги, которая встретилась хотя бы дважды.
    // ID книг и уникальные ID читателей отсортировать по возрастанию. Не изменять loans.
    static List<String> duplicateReport(List<Loan> loans) {
        Map<Integer, Set<Integer>> duplicateMap = new HashMap<>();
        Map<Integer, Integer> countsMap = new HashMap<>();

        for (Loan loan : loans) {
            countsMap.put(
                    loan.bookId,
                    countsMap.getOrDefault(loan.bookId, 0) + 1
            );

            duplicateMap
                    .computeIfAbsent(loan.bookId, id -> new HashSet<>())
                    .add(loan.readerId);
        }

        List<String> report = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : countsMap.entrySet()) {
            int bookId = entry.getKey();
            int count = entry.getValue();

            if (count > 1) {
                report.add(
                        "Book " + bookId +
                                ": " + count +
                                " loans, readers=" + duplicateMap.get(bookId)
                );
            }
        }

        return report;
    }

    public static void main(String[] args) {
        check("same reader twice",
                List.of("DUPLICATE book=40 readers=[9]"),
                duplicateReport(List.of(new Loan(40, 9), new Loan(40, 9))));
        check("two readers, reverse input",
                List.of("DUPLICATE book=40 readers=[2, 9]"),
                duplicateReport(List.of(new Loan(40, 9), new Loan(40, 2))));
        check("different books stay separate",
                List.of("DUPLICATE book=40 readers=[2, 9]",
                        "DUPLICATE book=70 readers=[5]"),
                duplicateReport(List.of(new Loan(70, 5), new Loan(40, 9),
                        new Loan(70, 5), new Loan(40, 2), new Loan(90, 1))));
        check("one occurrence", List.of(),
                duplicateReport(List.of(new Loan(40, 9))));
        check("empty input", List.of(), duplicateReport(List.of()));
        List<Loan> source = new ArrayList<>(List.of(new Loan(70, 5), new Loan(40, 9)));
        List<Loan> before = new ArrayList<>(source);
        duplicateReport(source);
        check("source unchanged", before, source);
        System.out.println("PASS: 6 grouping checks");
    }

    private static void check(String name, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
