package higor.mybooksapi.application.controller;

import higor.mybooksapi.application.facade.BookFacade;
import higor.mybooksapi.application.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/v1")
@Tag(name = "Books", description = "Books operations")
public class BookController {

  private final BookFacade facade;

  public BookController(BookFacade facade) {
    this.facade = facade;
  }

  @Operation(summary = "Find books", tags = "Books")
  @ApiResponses({
      @ApiResponse(responseCode = "200", headers = @Header(name = "ETag", ref = "#components/headers/ETag")),
      @ApiResponse(responseCode = "401", ref = "#/components/responses/401"),
      @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
      @ApiResponse(responseCode = "415", ref = "#/components/responses/415"),
      @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
  })
  @GetMapping("/books")
  public Page<BookDto> list(@RequestParam(name = "filter", required = false) String filter,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
      @RequestParam(value = "direction", required = false, defaultValue = "ASC") Sort.Direction direction,
      @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy) {
    return facade.list(filter, page, pageSize, direction, sortBy);
  }

  @Operation(summary = "Create a books", tags = "Books")
  @ApiResponses({
      @ApiResponse(responseCode = "201", headers = @Header(name = "Location", ref = "#/components/headers/Location")),
      @ApiResponse(responseCode = "401", ref = "#/components/responses/401"),
      @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
      @ApiResponse(responseCode = "409", ref = "#/components/responses/409"),
      @ApiResponse(responseCode = "415", ref = "#/components/responses/415"),
      @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
  })
  @PostMapping("/books")
  public ResponseEntity<Void> create(@RequestBody BookDto book) throws URISyntaxException {
    return ResponseEntity.created(new URI("http://localhost/v1/books/" + facade.create(book))).build();
  }
}
