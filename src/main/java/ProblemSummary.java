import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Часть 05/06. Сводка проблем по типам. */
public class ProblemSummary {
    /**
     * Сколько проблем каждого типа.
     * В ответе есть все типы — в порядке объявления в enum, даже если проблем этого типа нет.
     * Входной список не меняй.
     */
    public static Map<ProblemType, Integer> countByType(List<LibraryProblem> problems) {
        Map<ProblemType, Integer> result = new EnumMap<>(ProblemType.class);
        for (ProblemType type : ProblemType.values()) {
            result.put(type, 0);
        }
        for (LibraryProblem problem : problems) {
            ProblemType type = problem.type();
            result.put(type, result.get(type) + 1);
        }

        return result;
    }
}