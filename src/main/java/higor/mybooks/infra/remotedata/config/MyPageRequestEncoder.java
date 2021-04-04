package higor.mybooks.infra.remotedata.config;

import feign.CollectionFormat;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import higor.mybooks.domain.page.MyPageRequest;

import java.lang.reflect.Type;

public class MyPageRequestEncoder implements Encoder {
  private final Encoder delegate;

  public MyPageRequestEncoder(Encoder delegate) {
    this.delegate = delegate;
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
    if (object instanceof MyPageRequest) {
      MyPageRequest pageRequest = (MyPageRequest) object;
      template.collectionFormat(CollectionFormat.CSV);
      template.query("page", pageRequest.page + "");
      template.query("size", pageRequest.size + "");
      template.query("sort", pageRequest.sort);
    } else {
      delegate.encode(object, bodyType, template);
    }
  }
}
