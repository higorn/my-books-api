package higor.mybooks.infra.remotedata.book;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository2;
import higor.mybooks.domain.page.MyPage;
import higor.mybooks.domain.page.MyPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRemoteRepository2 implements BookRepository2 {
  private final BookClient bookClient;

  public BookRemoteRepository2(BookClient bookClient) {
    this.bookClient = bookClient;
  }

  @Override
  public MyPage<Book> findByTerm(String term, MyPageRequest pageRequest) {
    Pageable pageable = PageRequest.of(pageRequest.page, pageRequest.size, Sort.by("a,b,c"));
//    Pageable pageable = PageRequest.of(pageRequest.page, pageRequest.size, Sort.by(pageRequest.sort));
//    return toPage(bookClient.findByTerm(term, pageable), pageRequest);
//    return toPage(bookClient.findByTerm2(term, pageRequest.page, pageRequest.size, pageRequest.sort), pageRequest);
    return toPage(bookClient.findByTerm2(term, pageRequest), pageRequest);
  }

  private MyPage<Book> toPage(PagedModel<EntityModel<Book>> pagedEntityModel, MyPageRequest pageRequest) {
    PagedModel.PageMetadata metadata = pagedEntityModel.getMetadata();
    MyPageRequest newPageRequest = MyPageRequest.of(pageRequest, (int) metadata.getNumber(), (int) metadata.getSize());
    List<Book> entityList = pagedEntityModel.getContent().stream().map(this::toEntity).collect(Collectors.toList());
    return MyPage.of(entityList, newPageRequest, metadata.getTotalElements());
  }

  protected Book toEntity(EntityModel<Book> entityModel) {
    String href = entityModel.getLink("self").get().getHref();
    Integer id = Integer.valueOf(href.substring(href.lastIndexOf("/") + 1));
    return entityModel.getContent().id(id);
  }
}
