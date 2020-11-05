package higor.mybooksapi.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import higor.mybooksapi.application.config.TestConfig;
import higor.mybooksapi.application.dto.BookDto;
import higor.mybooksapi.application.facade.BookFacade;
import higor.mybooksapi.application.utils.StubJwt;
import higor.mybooksapi.domain.exception.DuplicatedEntryException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static higor.mybooksapi.application.controller.ControllerTestConstants.AUTHORIZATION_HEADER;
import static higor.mybooksapi.application.controller.ControllerTestConstants.AUTH_TYPE;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ContextConfiguration(classes = { TestConfig.class })
class BookControllerTest {

  @Autowired
  private MockMvc    mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private JwtDecoder jwtDecoder;
  @MockBean
  private BookFacade facade;

  @Test
  void givenANotAuthenticatedRequest_thenReturnsUnauthorizedStatus() throws Exception {
    mockMvc.perform(get("/v1/books"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenThatThereAreNoBooks_whenListBooks_thenReturnEmptyList() throws Exception {
    Page<BookDto> books = new PageImpl<>(new ArrayList<>());
    StubJwt stubJwt = new StubJwt();
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());
    when(facade.list(any(), anyInt(), anyInt(), any(), any())).thenReturn(books);

    mockMvc.perform(get("/v1/books")
        .header(AUTHORIZATION_HEADER, AUTH_TYPE + stubJwt.getToken()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.content", hasSize(0)));

    verify(jwtDecoder).decode(anyString());
    verify(facade).list(any(), anyInt(), anyInt(), any(), any());
  }

  @Test
  void whenListOfBooksWithInvalidParams_thenReturnBadRequestStatus() throws Exception {
    StubJwt stubJwt = new StubJwt();
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());
    when(facade.list(any(), anyInt(), anyInt(), any(), any())).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(get("/v1/books")
        .header(AUTHORIZATION_HEADER, AUTH_TYPE + stubJwt.getToken()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty());

    verify(jwtDecoder).decode(anyString());
    verify(facade).list(any(), anyInt(), anyInt(), any(), any());
  }

  @Test
  void givenNoBook_whenCreate_thenReturnsBadRequestStatus() throws Exception {
    StubJwt stubJwt = new StubJwt();
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());

    doPost(stubJwt, "")
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.message", containsString("Required request body is missing")));

    verify(jwtDecoder).decode(anyString());
  }

  @Test
  void givenABook_whenCreate_andConversionFails_thenReturnsBadRequestStatus() throws Exception {
    StubJwt stubJwt = new StubJwt();
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());
    when(facade.create(any(BookDto.class))).thenThrow(ConversionFailedException.class);

    doPost(stubJwt, getContent())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty());

    verify(jwtDecoder).decode(anyString());
    verify(facade).create(any(BookDto.class));
  }

  @Test
  void givenABook_whenItIsADuplicatedEntry_thenReturnsConflictStatus() throws Exception {
    String message = "Duplicate entry";
    StubJwt stubJwt = new StubJwt();
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());
    when(facade.create(any(BookDto.class))).thenThrow(new DuplicatedEntryException(message));

    doPost(stubJwt, getContent())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("CONFLICT")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.message", is(message)));

    verify(jwtDecoder).decode(anyString());
    verify(facade).create(any(BookDto.class));
  }

  @Test
  void givenABook_whenIsValid_thenCreate_andReturnsCreatedStatus_andReturnsTheLocationHeader() throws Exception {
    StubJwt stubJwt = new StubJwt();
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());
    when(facade.create(any(BookDto.class))).thenReturn(1);

    doPost(stubJwt, getContent())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString("http://localhost/v1/books")));

    verify(jwtDecoder).decode(anyString());
    verify(facade).create(any(BookDto.class));
  }

  private ResultActions doPost(StubJwt stubJwt, String content) throws Exception {
    return mockMvc.perform(
        post("/v1/books").header(AUTHORIZATION_HEADER, AUTH_TYPE + stubJwt.getToken())
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  private String getContent() throws JsonProcessingException {
    BookDto bookDto = new BookDto();
    bookDto.title = "teste";
    bookDto.author = "Nicanor";
    return mapper.writeValueAsString(bookDto);
  }
}