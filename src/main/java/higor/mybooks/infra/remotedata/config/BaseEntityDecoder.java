package higor.mybooks.infra.remotedata.config;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import higor.mybooks.domain.BaseEntity;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseEntityDecoder implements Decoder {
  private final Decoder delegate;
  public BaseEntityDecoder(Decoder delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException, FeignException {
    if (!isBaseEntity(type))
      return delegate.decode(response, type);
    return toEntity(getEntityModel(response, type));
  }

  private boolean isBaseEntity(Type type) {
    if (type instanceof ParameterizedType) {
      return false;
    }
    return BaseEntity.class.isAssignableFrom((Class<?>) type);
  }

  private EntityModel<BaseEntity<? super Object>> getEntityModel(Response response, Type type) throws IOException {
    ParameterizedType parameterizedType = TypeUtils.parameterize(EntityModel.class, type);
    return (EntityModel<BaseEntity<? super Object>>) delegate.decode(response, parameterizedType);
  }

  static BaseEntity<? super Object> toEntity(EntityModel<BaseEntity<? super Object>> entityModel) {
    BaseEntity<? super Object> content = entityModel.getContent();
    entityModel.getLink("self")
        .map(Link::getHref)
        .map(href -> Integer.valueOf(href.substring(href.lastIndexOf("/") + 1)))
        .ifPresent(content::setId);
    return content;
  }
}
