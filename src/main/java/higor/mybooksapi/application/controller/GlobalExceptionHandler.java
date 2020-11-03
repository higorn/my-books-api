package higor.mybooksapi.application.controller;

import higor.mybooksapi.application.facade.dto.ErrorDto;
import higor.mybooksapi.domain.exception.DuplicatedEntryException;
import javassist.NotFoundException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return ResponseEntity.status(status).body(new ErrorDto(status, e.getMessage()));
  }

  @ExceptionHandler({ ConversionFailedException.class, IllegalArgumentException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorDto> handleBadRequest(Exception e) {
    return handleExceptionWithContent(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ NotFoundException.class })
  public ResponseEntity<Object> handleNotFound(Exception e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler({ DuplicatedEntryException.class })
  public ResponseEntity<ErrorDto> handleConflict(Exception e) {
    return handleExceptionWithContent(e, HttpStatus.CONFLICT);
  }

  private ResponseEntity<ErrorDto> handleExceptionWithContent(Exception e, HttpStatus status) {
    return ResponseEntity.status(status).body(new ErrorDto(status, e.getMessage()));
  }
}
