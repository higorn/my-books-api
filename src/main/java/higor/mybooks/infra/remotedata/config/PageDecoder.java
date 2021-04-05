package higor.mybooks.infra.remotedata.config;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import higor.mybooks.domain.BaseEntity;
import higor.mybooks.domain.page.Page;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class PageDecoder implements Decoder {
  private final Decoder delegate;

  public PageDecoder(Decoder delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException, FeignException {
    if (!isPage(type)) {
      return delegate.decode(response, type);
    }
    ParameterizedType parameterizedType = TypeUtils
        .parameterize(EntityModel.class, ((ParameterizedType) type).getActualTypeArguments());
    ParameterizedType parameterizedType2 = TypeUtils
        .parameterize(PagedModel.class, parameterizedType);
//    ParameterizedType parameterizedType2 = TypeUtils
//        .parameterize(PagedModel.class, ((ParameterizedType) type).getActualTypeArguments());
    PagedModel pagedModel = (PagedModel) delegate.decode(response, parameterizedType2);
    PagedModel.PageMetadata metadata = pagedModel.getMetadata();
//    List entityList = pagedModel.getContent().stream().map(PageDecoder::toEntity).collect(Collectors.toList());
    return Page.of(pagedModel.getContent(), Page.Metadata.of((int)metadata.getNumber(), (int)metadata.getSize(),
        metadata.getTotalElements()));
  }

  static <T extends BaseEntity<T, ID>, ID> BaseEntity<T,ID> toEntity(EntityModel<T> entityModel) {
    String href = entityModel.getLink("self").get().getHref();
    Integer id = Integer.valueOf(href.substring(href.lastIndexOf("/") + 1));
    return entityModel.getContent().id((ID)id);
  }

  static boolean isPage(Type type) {
    if (!(type instanceof ParameterizedType)) {
      return false;
    }
    ParameterizedType parameterizedType = (ParameterizedType) type;
    return parameterizedType.getRawType().equals(Page.class);
  }
}
