package higor.mybooksapi.application.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StubJwt {

  private Map<String, Object> header;
  private Map<String, Object> claims;
  private String              token;


  public StubJwt() {
    header = new HashMap<>();
    header.put("typ", "JWT");
    Instant iat = Instant.now();
    Instant exp = iat.plus(Duration.ofDays(1));
    claims = new HashMap<>();
    claims.put("iss", "my-books");
    claims.put("aud", "api://default");
    claims.put("iat", iat);
    claims.put("exp", exp);
    claims.put("cid", "abd");
    claims.put("uid", "abc");
    claims.put("sub", "user@test.com");
  }

  public StubJwt groups(String... groups) {
    claims.put("groups", groups);
    return this;
  }

  public String getToken() {
    if (token == null)
      buildToken();
    return token;
  }

  public Jwt toJwt() {
    return Jwt.withTokenValue(getToken())
        .headers(hdrs -> hdrs.putAll(header))
        .claims(clms -> clms.putAll(claims))
        .build();
  }

  private void buildToken() {
    token = Jwts.builder()
        .setHeader(header)
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS512, "secret")
        .compact();
  }
}
