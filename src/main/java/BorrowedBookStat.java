import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BorrowedBookStat {
    private long count;
    private List<String> readerNames;
}