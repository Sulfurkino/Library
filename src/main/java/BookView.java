import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class BookView {
    private int year;
    private String bookName;
    private int id;
    private boolean isAvailable;
}
