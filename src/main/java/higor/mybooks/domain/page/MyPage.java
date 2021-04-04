package higor.mybooks.domain.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MyPage<T> implements /*Iterable<T>, Supplier<Stream<T>>,*/ Serializable {
  private final List<T> content = new ArrayList<>();
  private final MyPageRequest pageRequest;
  private final long total;

  private MyPage(List<T> content, MyPageRequest pageRequest, long total) {
    this.content.addAll(content);
    this.pageRequest = pageRequest;
    this.total = total;
  }

  private MyPage(List<T> content) {
    this(content, MyPageRequest.unpaged(), null == content ? 0 : content.size());
  }

  public static <U> MyPage<U> of(List<U> content) {
    return new MyPage<>(content);
  }

  public static <U> MyPage<U> of(List<U> content, MyPageRequest pageRequest, long total) {
    return new MyPage<>(content, pageRequest, total);
  }

  public int getPageNumber() {
    return pageRequest.page;
  }

  public int getPageSize() {
    return pageRequest.size;
  }

  public long getOffset() {
    return (long) pageRequest.page * (long) pageRequest.size;
  }

  public int getTotalPages() {
    return getPageSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getPageSize());
  }

  public long getTotalElements() {
    return total;
  }

  public int getContentSize() {
    return content.size();
  }

  public boolean isFirst() {
    return getPageNumber() == 0;
  }

  public boolean isLast() {
    return getPageNumber() + 1 == getTotalPages();
  }

  public List<T> getContent() {
    return Collections.unmodifiableList(content);
  }

  public <U> MyPage<U> map(Function<? super T, ? extends U> converter) {
    return new MyPage<>(getConvertedContent(converter), pageRequest, total);
  }

  private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
    return content.stream().map(converter).collect(Collectors.toList());
  }
/*

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }

  @Override
  public Stream<T> get() {
    return stream();
  }

  Stream<T> stream() {
    return StreamSupport.stream(spliterator(), false);
  }
*/
}
