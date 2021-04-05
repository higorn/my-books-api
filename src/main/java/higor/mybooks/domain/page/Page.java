package higor.mybooks.domain.page;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> {
  private final Collection<T> content;
  private final Metadata      metadata;

  public Page() {
    this(new ArrayList<>());
  }

  protected Page(Collection<T> content) {
    this(content, Metadata.unpaged());
  }

  protected Page(Collection<T> content, Metadata metadata) {
    this.content = new ArrayList<>();
    this.content.addAll(content);
    this.metadata = metadata;
  }

  public static <U> Page<U> of(Collection<U> content) {
    return new Page<>(content);
  }

  public static <U> Page<U> of(Collection<U> content, Metadata metadata) {
    return new Page<>(content, metadata);
  }

  public int getPageNumber() {
    return metadata.page;
  }

  public int getPageSize() {
    return metadata.size;
  }

  public long getOffset() {
    return (long) metadata.page * (long) metadata.size;
  }

  public int getTotalPages() {
    return metadata.totalPages;
  }

  public long getTotalElements() {
    return metadata.totalElements;
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

  public Collection<T> getContent() {
    return Collections.unmodifiableCollection(content);
  }

  public <U> Page<U> map(Function<? super T, ? extends U> converter) {
    return new Page<>(getConvertedContent(converter), metadata);
  }

  private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
    return content.stream().map(converter).collect(Collectors.toList());
  }

  public static class Metadata {
    public final int  page;
    public final int  size;
    public final long totalElements;
    public final int  totalPages;

    private Metadata(int page, int size, long totalElements, int totalPages) {

      Assert.isTrue(page > -1, "Page must not be negative!");
      Assert.isTrue(size > -1, "Size must not be negative!");
      Assert.isTrue(totalElements > -1, "Total elements must not be negative!");
      Assert.isTrue(totalPages > -1, "Total pages must not be negative!");

      this.page = page;
      this.size = size;
      this.totalElements = totalElements;
      this.totalPages = totalPages;
    }

    private Metadata(int page, int size, long totalElements) {
      this(page, size, totalElements, size == 0 ? 0 : (int) Math.ceil((double) totalElements / (double) size));
    }

    public static Metadata of(int page, int size, long total) {
      return new Metadata(page, size, total);
    }

    public static Metadata unpaged() {
      return new Metadata(0, 0, 0);
    }
  }
}
