import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Library {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Reader> readers = new HashMap<>();

}

