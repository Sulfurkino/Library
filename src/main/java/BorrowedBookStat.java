import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@ToString
@Setter
@Getter
@AllArgsConstructor
public class BorrowedBookStat {
    private long count;
    private List<String> readerNames;
}