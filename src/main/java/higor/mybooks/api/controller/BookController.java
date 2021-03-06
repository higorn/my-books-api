package higor.mybooks.api.controller;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.facade.BookFacade;
import higor.mybooks.application.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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
  private final UserFacade userFacade;

  public BookController(BookFacade facade, UserFacade userFacade) {
    this.facade = facade;
    this.userFacade = userFacade;
  }

  @Operation(summary = "Find books", tags = "Books")
  @ApiResponses({
      @ApiResponse(responseCode = "200", headers = @Header(name = "ETag", ref = "#components/headers/ETag")),
      @ApiResponse(responseCode = "401", ref = "#/components/responses/401"),
      @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
      @ApiResponse(responseCode = "415", ref = "#/components/responses/415"),
      @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
  })
  @SecurityRequirements // Shows as public resource in the api-doc. Does not require authentication
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
    return ResponseEntity.created(new URI("http://localhost/v1/books/"
        + facade.create(book, userFacade.getUser().get()))).build();
  }

  // TODO: get book detail
}
