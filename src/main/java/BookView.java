import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookView {
    private int year;
    private String bookName;
    private int id;
    private boolean isAvailable;
}
