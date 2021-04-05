package higor.mybooks.domain.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> implements /*Iterable<T>, Supplier<Stream<T>>,*/ Serializable {
  private final List<T>     content = new ArrayList<>();
  private final PageRequest pageRequest;
  private final long        total;

  private Page(List<T> content, PageRequest pageRequest, long total) {
    this.content.addAll(content);
    this.pageRequest = pageRequest;
    this.total = total;
  }

  private Page(List<T> content) {
    this(content, PageRequest.unpaged(), null == content ? 0 : content.size());
  }

  public static <U> Page<U> of(List<U> content) {
    return new Page<>(content);
  }

  public static <U> Page<U> of(List<U> content, PageRequest pageRequest, long total) {
    return new Page<>(content, pageRequest, total);
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

  public <U> Page<U> map(Function<? super T, ? extends U> converter) {
    return new Page<>(getConvertedContent(converter), pageRequest, total);
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
