package higor.mybooks.application.security;

import net.minidev.json.JSONArray;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtGroupsAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    Collection<GrantedAuthority> authorities = Stream.concat(grantedAuthoritiesConverter.convert(jwt)
        .stream(), extractGroups(jwt).stream()).collect(Collectors.toSet());
    return new JwtAuthenticationToken(jwt, authorities);
  }

  private Collection<? extends GrantedAuthority> extractGroups(Jwt jwt) {
    JSONArray groups = jwt.getClaim("groups");
    if (groups == null || groups.isEmpty())
      return Collections.emptySet();
    return groups.stream().map(g -> new SimpleGrantedAuthority(g.toString())).collect(Collectors.toSet());
  }
}
