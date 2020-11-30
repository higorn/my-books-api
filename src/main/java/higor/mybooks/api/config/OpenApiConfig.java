package higor.mybooks.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Stream;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("My books API")
            .description("My Books API"))
        .servers(getServers())
        .security(getSecurityRequirements())
        .components(new Components()
            .securitySchemes(getSecuritySchemes())
            .headers(getHeaders())
            .responses(getResponses()));
  }

  private List<Server> getServers() {
    return null;
  }

  private List<SecurityRequirement> getSecurityRequirements() {
//    return Collections.singletonList(new SecurityRequirement().addList("google"));
    return Arrays.asList(
        new SecurityRequirement().addList("google"),
        new SecurityRequirement().addList("okta"));
  }

  private Map<String, SecurityScheme> getSecuritySchemes() {
    Map<String, SecurityScheme> securitySchemes = new HashMap<>();
    securitySchemes.put("google", new SecurityScheme()
        .type(SecurityScheme.Type.OAUTH2)
        .extensions(getSecurityExtentions())
        .flows(new OAuthFlows().implicit(new OAuthFlow()
            .authorizationUrl("https://accounts.google.com/o/oauth2/v2/auth")
//            .tokenUrl("https://oauth2.googleapis.com/token")
            .scopes(new Scopes()
                .addString("openid", "OpenID information")
                .addString("profile", "Profile information")
                .addString("email", "E-Mail address")))));
    securitySchemes.put("okta", new SecurityScheme()
        .type(SecurityScheme.Type.OAUTH2)
        .flows(new OAuthFlows().authorizationCode(new OAuthFlow()
            .authorizationUrl("https://dev-2762279.okta.com/oauth2/default/v1/authorize")
            .tokenUrl("https://dev-2762279.okta.com/oauth2/default/v1/token")
            .scopes(new Scopes()
                .addString("openid", "OpenID information")
                .addString("profile", "Profile information")
                .addString("email", "E-Mail address")))));
    return securitySchemes;
  }

  private Map<String, Object> getSecurityExtentions() {
    Map<String, Object> extentions = new HashMap<>();
    extentions.put("x-google-issuer", "https://accounts.google.com");
    extentions.put("x-google-jwks_uri", "https://www.googleapis.com/oauth2/v3/certs");
    extentions.put("x-google-audiences", "635953397895-hkpkmjro2hrtp3as1k396vi44oqc9kll.apps.googleusercontent.com");
    return extentions;
  }

  private Map<String, Header> getHeaders() {
    Map<String, Header> headers = new HashMap<>();
    headers.put("ETag", new Header()
        .description("Content md5 hash")
        .schema(new Schema().title("string").example("14513421341cdadsf452435234a90ab0a")));
    headers.put("Location", new Header()
        .description("The resource location")
        .schema(new Schema().title("string").example("http://www.api.com/v1/books/1")));
    return headers;
  }

  private Map<String, ApiResponse> getResponses() {
    Map<String, ApiResponse> responses = new HashMap<>();
    Stream.of(HttpStatus.BAD_REQUEST, HttpStatus.CONFLICT, HttpStatus.UNSUPPORTED_MEDIA_TYPE, HttpStatus.INTERNAL_SERVER_ERROR).
        forEach(status -> responses.put(Integer.valueOf(status.value()).toString(), createErrorResponse(status)));
    Stream.of(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN, HttpStatus.NOT_FOUND)
        .forEach(status -> responses.put(Integer.valueOf(status.value()).toString(), new ApiResponse().description(status.getReasonPhrase())));
    return responses;
  }

  private ApiResponse createErrorResponse(HttpStatus status) {
    return new ApiResponse()
        .description(status.getReasonPhrase())
        .content(new Content()
            .addMediaType("application/json", new MediaType().schema(new Schema().$ref("Error")))
            .addMediaType("application/xml", new MediaType().schema(new Schema().$ref("Error"))));
  }
}
