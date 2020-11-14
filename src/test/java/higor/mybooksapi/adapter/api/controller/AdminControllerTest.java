package higor.mybooksapi.adapter.api.controller;

import higor.mybooksapi.api.controller.AdminController;
import higor.mybooksapi.application.config.TestConfig;
import higor.mybooksapi.application.utils.StubJwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static higor.mybooksapi.application.controller.ControllerTestConstants.AUTHORIZATION_HEADER;
import static higor.mybooksapi.application.controller.ControllerTestConstants.AUTH_TYPE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@ContextConfiguration(classes = { TestConfig.class })
public class AdminControllerTest {

  @Autowired
  private MockMvc      mockMvc;
  @MockBean
  private JwtDecoder   jwtDecoder;

  @Test
  void givenIHaveAdminRights_whenListUsers_thenReturnUsers() throws Exception {
    StubJwt stubJwt = new StubJwt().groups("Admin", "User");
    when(jwtDecoder.decode(anyString())).thenReturn(stubJwt.toJwt());

    mockMvc.perform(get("/v1/admin/users")
          .header(AUTHORIZATION_HEADER, AUTH_TYPE + stubJwt.getToken()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.content", hasSize(0)));

    verify(jwtDecoder).decode(anyString());
  }
}
