package higor.mybooks.domain.page;

import java.util.Arrays;

public class PageRequest {
  public enum SortDirection {
    ASC, DESC
  }

  public final int    page;
  public final int    size;
  public final String sort;

  private PageRequest(int page, int size, String... sort) {
    this.page = page;
    this.size = size;
    this.sort = String.join(",", Arrays.asList(sort));
  }

  public static PageRequest of(int page, int size, String sort) {
    return new PageRequest(page, size, sort);
  }

  public static PageRequest of(int page, int size, String sort, SortDirection direction) {
    return new PageRequest(page, size, sort, direction.name());
  }

  public static PageRequest of(PageRequest pageRequest, int page, int size) {
    return new PageRequest(page, size, pageRequest.sort);
  }

  @Override
  public String toString() {
    return String.format("Page request [number: %d, size %d, sort: %s]", page, size, sort);
  }
}
