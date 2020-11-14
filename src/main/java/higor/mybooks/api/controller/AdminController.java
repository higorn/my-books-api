package higor.mybooks.api.controller;

import higor.mybooks.api.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/v1/admin")
@Tag(name = "Admin", description = "Admin operations")
public class AdminController {

  @Operation(summary = "List users", tags = "Admin")
  @ApiResponses({
      @ApiResponse(responseCode = "200", headers = @Header(name = "ETag", ref = "#components/headers/ETag")),
      @ApiResponse(responseCode = "401", ref = "#/components/responses/401"),
      @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
      @ApiResponse(responseCode = "415", ref = "#/components/responses/415"),
      @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
  })
  @PreAuthorize("hasAuthority('Admin')")
  @GetMapping("/users")
  public Page<UserDto> list(@RequestParam(name = "filter", required = false) String filter) {
    Page<UserDto> users = new PageImpl<>(new ArrayList<>());
    return users;
  }
}
