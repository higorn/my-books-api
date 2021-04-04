package higor.mybooks.domain.exception;

public class DuplicatedEntryException extends RuntimeException {
  public DuplicatedEntryException(String message) {
    super(message);
  }
}
