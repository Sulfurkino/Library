import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@ToString
@Setter
@Getter
@AllArgsConstructor
public class LibraryProblem {
    private String exceptionType;
    private Set<Integer> bookId;
    private Set<Integer> readerIds;

}
