package higor.mybooks.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import higor.mybooks.domain.exception.DuplicatedEntryException;
import higor.mybooks.domain.user.User;
import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.config.TestConfig;
import higor.mybooks.application.facade.BookFacade;
import higor.mybooks.application.facade.UserFacade;
import higor.mybooks.application.utils.StubJwt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.text.ParseException;
import java.util.ArrayList;

import static java.util.Optional.of;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ContextConfiguration(classes = { TestConfig.class })
class BookControllerTest {

  @Autowired
  private MockMvc      mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private JwtDecoder   jwtDecoder;
  @MockBean
  private BookFacade facade;
  @MockBean
  private UserFacade   userFacade;
  private User         stubUser;
  private StubJwt      stubJwt;

  @BeforeEach
  void setUp() throws ParseException {
    stubJwt = new StubJwt("nicanor@email.com");
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());

    stubUser = new User();
    stubUser.setId(1);
    stubUser.setEmail((String) stubJwt.toJwt().getClaims().get("sub"));
    when(userFacade.getUser()).thenReturn(of(stubUser));
  }

  @AfterEach
  void tearDown() {
    reset(jwtDecoder);
  }

  @Test
  void whenCreateABookWithoutAuthentication_thenReturnsUnauthorizedStatus() throws Exception {
    mockMvc.perform(post("/v1/books").with(csrf())) // post without authentication must have csrf
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void whenCreateABookWithoutAuthentication_andWithoutCsrf_thenReturnsForbiddenStatus() throws Exception {
    mockMvc.perform(post("/v1/books")) // post without authentication must have csrf
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenThatThereAreNoBooks_whenListBooks_thenReturnEmptyList() throws Exception {
    Page<BookDto> books = new PageImpl<>(new ArrayList<>());
    when(facade.list(any(), anyInt(), anyInt(), any(), any())).thenReturn(books);

    mockMvc.perform(get("/v1/books"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.content", hasSize(0)));

    verify(facade).list(any(), anyInt(), anyInt(), any(), any());
  }

  @Test
  void whenListOfBooksWithInvalidParams_thenReturnBadRequestStatus() throws Exception {
    when(facade.list(any(), anyInt(), anyInt(), any(), any())).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(get("/v1/books"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty());

    verify(facade).list(any(), anyInt(), anyInt(), any(), any());
  }

  @Test
  void givenNoBook_whenCreate_thenReturnsBadRequestStatus() throws Exception {
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
    when(facade.create(any(BookDto.class), any(User.class))).thenThrow(ConversionFailedException.class);

    doPost(stubJwt, getContent())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty());

    verify(jwtDecoder).decode(anyString());
    verify(facade).create(any(BookDto.class), any(User.class));
  }

  @Test
  void givenABook_whenItIsADuplicatedEntry_thenReturnsConflictStatus() throws Exception {
    String message = "Duplicate entry";
    when(facade.create(any(BookDto.class), any(User.class))).thenThrow(new DuplicatedEntryException(message));

    doPost(stubJwt, getContent())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.status", is("CONFLICT")))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.message", is(message)));

    verify(jwtDecoder).decode(anyString());
    verify(facade).create(any(BookDto.class), any(User.class));
  }

  @Test
  void givenABook_whenIsValid_thenCreate_andReturnsCreatedStatus_andReturnsTheLocationHeader() throws Exception {
    doAnswer(invocation -> {
      User u = invocation.getArgument(1, User.class);
      assertEquals(stubUser.getId(), u.getId());
      assertEquals(stubUser.getEmail(), u.getEmail());
      return 1;
    }).when(facade).create(any(BookDto.class), any(User.class));

    doPost(stubJwt, getContent())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString("http://localhost/v1/books")));

    verify(jwtDecoder).decode(anyString());
    verify(userFacade).getUser();
    verify(facade).create(any(BookDto.class), any(User.class));
  }

  private ResultActions doPost(StubJwt stubJwt, String content) throws Exception {
    return mockMvc.perform(
        post("/v1/books").header(ControllerTestConstants.AUTHORIZATION_HEADER, ControllerTestConstants.AUTH_TYPE + stubJwt.getToken())
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  private String getContent() throws JsonProcessingException {
    return mapper.writeValueAsString(new BookDto().title("teste").author("Nicanor"));
  }
}