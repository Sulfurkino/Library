import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class LibraryProblem {
    private String exceptionType;
    private Set<Integer> bookId;
    private List<Integer> readerIds;

}
