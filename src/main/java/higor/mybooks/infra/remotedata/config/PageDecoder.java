package higor.mybooks.infra.remotedata.config;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import higor.mybooks.domain.BaseEntity;
import higor.mybooks.domain.page.Page;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class PageDecoder implements Decoder {
  private final Decoder delegate;

  public PageDecoder(Decoder delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException, FeignException {
    if (!isPage(type))
      return delegate.decode(response, type);

    PagedModel<EntityModel<BaseEntity<? super Object>>> pagedModel = getPagedModel(response, type);
    return Page.of(getEntityList(pagedModel), getPageMetadata(pagedModel.getMetadata()));
  }

  static boolean isPage(Type type) {
    if (!(type instanceof ParameterizedType)) {
      return false;
    }
    ParameterizedType parameterizedType = (ParameterizedType) type;
    return parameterizedType.getRawType().equals(Page.class);
  }

  private PagedModel<EntityModel<BaseEntity<? super Object>>> getPagedModel(Response response, Type type)
      throws IOException {
    ParameterizedType parameterizedType = getParameterizedType((ParameterizedType) type);
    return (PagedModel<EntityModel<BaseEntity<? super Object>>>) delegate.decode(response, parameterizedType);
  }

  private ParameterizedType getParameterizedType(ParameterizedType type) {
    ParameterizedType parameterizedType = TypeUtils.parameterize(EntityModel.class, type.getActualTypeArguments());
    return TypeUtils.parameterize(PagedModel.class, parameterizedType);
  }

  private List<BaseEntity<? super Object>> getEntityList(PagedModel<EntityModel<BaseEntity<? super Object>>> pagedModel) {
    return pagedModel.getContent().stream()
        .map(PageDecoder::toEntity)
        .collect(Collectors.toList());
  }

  static BaseEntity<? super Object> toEntity(EntityModel<BaseEntity<? super Object>> entityModel) {
    BaseEntity<? super Object> content = entityModel.getContent();
    entityModel.getLink("self")
        .map(Link::getHref)
        .map(href -> Integer.valueOf(href.substring(href.lastIndexOf("/") + 1)))
        .ifPresent(content::setId);
    return content;
  }

  private Page.Metadata getPageMetadata(PagedModel.PageMetadata metadata) {
    return Page.Metadata.of((int) metadata.getNumber(), (int) metadata.getSize(), metadata.getTotalElements());
  }
}
