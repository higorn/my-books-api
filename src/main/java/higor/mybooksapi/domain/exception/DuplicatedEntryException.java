package higor.mybooksapi.domain.exception;

public class DuplicatedEntryException extends RuntimeException {
  public DuplicatedEntryException(String message) {
    super(message);
  }
}
