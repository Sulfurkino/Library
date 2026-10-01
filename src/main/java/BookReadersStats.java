import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BookReadersStats {
    private int occurrences;
    private final Set<Integer> readerIds = new HashSet<>();

    public void add(int readerId) {
        occurrences++;
        readerIds.add(readerId);
    }
}