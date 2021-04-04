package higor.mybooks.domain.page;

import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;

public class MyPageRequest {
  public enum SortDirection {
    ASC, DESC
  }

  public final int    page;
  public final int    size;
  public final String sort;

  private MyPageRequest(int page, int size, String... sort) {
    this.page = page;
    this.size = size;
    this.sort = Strings.join(Arrays.asList(sort), ',');
  }

  public static MyPageRequest of(int pageNumber, int pageSize, String sortFields, SortDirection sortDirection) {
    return new MyPageRequest(pageNumber, pageSize, sortFields, sortDirection.name());
  }

  public static MyPageRequest of(MyPageRequest pageRequest, int pageNumber, int pageSize) {
    return new MyPageRequest(pageNumber, pageSize, pageRequest.sort);
  }

  public static MyPageRequest unpaged() {
    return null;
  }

  @Override
  public String toString() {
    return String.format("Page request [number: %d, size %d, sort: %s]", page, size, sort);
  }
}
