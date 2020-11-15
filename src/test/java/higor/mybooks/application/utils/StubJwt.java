package higor.mybooks.application.utils;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StubJwt {

  private Map<String, Object> header;
  private Map<String, Object> claims;
  private String              token;


  public StubJwt() {
    this("nicanor@email.com");
  }

  public StubJwt(String sub) {
    header = new HashMap<>();
    header.put("typ", "JWT");
    claims = new HashMap<>();
    claims.put("iss", "my-books");
    claims.put("aud", "api://default");
    claims.put("cid", "abd");
    claims.put("uid", "abc");
    claims.put("sub", sub);
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

  public Jwt toJwt() throws ParseException {
    JWT jwt = JWTParser.parse(getToken());
    MappedJwtClaimSetConverter claimSetConverter = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
    Map<String, Object> convertedClaims = claimSetConverter.convert(jwt.getJWTClaimsSet().getClaims());

    return Jwt.withTokenValue(getToken())
        .headers(hdrs -> hdrs.putAll(header))
        .claims(clms -> clms.putAll(convertedClaims))
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
