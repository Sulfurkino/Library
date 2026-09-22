import lombok.*;


@Setter
@Getter
@AllArgsConstructor
public class OperationResult {

    private final boolean successful;
    private final String message;
}