package higor.mybooks.domain.page;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {

  @Nested
  @DisplayName("given a list of seven elements")
  class SevenElementsListTest {

    private List<String> elemens;

    @BeforeEach
    void givenAListOfSevenElements() {
      elemens = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
    }

    @Nested
    @DisplayName("when page size is two")
    class PageSizeTests {

      private int pageSize;

      @BeforeEach
      void whenPageSizeIsTwo() {
        pageSize = 2;
      }

      @Test
      void thenReturnsTheFirstPage() {
        PageRequest pageRequest = PageRequest.of(0, pageSize, "");

        Page<String> page = Page.of(elemens.stream()
            .filter(e -> e.equals("1") || e.equals("2"))
            .collect(Collectors.toList()), pageRequest, elemens.size());

        assertPage(page, 0, 0, 2, true, false);
      }

      @Test
      void thenReturnsTheSecondPage() {
        PageRequest pageRequest = PageRequest.of(1, pageSize, "");

        Page<String> page = Page.of(elemens.stream()
            .filter(e -> e.equals("3") || e.equals("4"))
            .collect(Collectors.toList()), pageRequest, elemens.size());

        assertPage(page, 1, 2, 2, false, false);
      }

      @Test
      void thenReturnsTheThirdPage() {
        PageRequest pageRequest = PageRequest.of(2, pageSize, "");

        Page<String> page = Page.of(elemens.stream()
            .filter(e -> e.equals("5") || e.equals("6"))
            .collect(Collectors.toList()), pageRequest, elemens.size());

        assertPage(page, 2, 4, 2, false, false);
      }

      @Test
      void thenReturnsTheFourthdPage() {
        PageRequest pageRequest = PageRequest.of(3, pageSize, "");

        Page<String> page = Page.of(elemens.stream()
            .filter(e -> e.equals("7"))
            .collect(Collectors.toList()), pageRequest, elemens.size());

        assertPage(page, 3, 6, 1, false, true);
      }

      private void assertPage(Page<String> page, int pageNumber, long offset, int contentSize, boolean isFirst,
          boolean isLast) {
        assertEquals(pageNumber, page.getPageNumber());
        assertEquals(2, page.getPageSize());
        assertEquals(offset, page.getOffset());
        assertEquals(4, page.getTotalPages());
        assertEquals(7, page.getTotalElements());
        assertEquals(contentSize, page.getContentSize());
        assertEquals(isFirst, page.isFirst());
        assertEquals(isLast, page.isLast());
      }
    }

    @Nested
    @DisplayName("when convert content")
    class ContentConvertionTests {

      @Test
      void thenReturnsAConvertedContentPage() {
        PageRequest pageRequest = PageRequest.of(0, 3, "");

        Page<String> page = Page.of(elemens.stream()
            .filter(e -> e.equals("1") || e.equals("2") || e.equals("3"))
            .collect(Collectors.toList()), pageRequest, elemens.size());
        Page<Integer> convertedPage = page.map(Integer::parseInt);

        assertEquals(6, convertedPage.getContent().stream().reduce(0, Integer::sum));
        assertEquals(0, convertedPage.getPageNumber());
        assertEquals(3, convertedPage.getPageSize());
        assertEquals(0, convertedPage.getOffset());
        assertEquals(3, convertedPage.getTotalPages());
        assertEquals(7, convertedPage.getTotalElements());
        assertEquals(3, convertedPage.getContentSize());
        assertTrue(convertedPage.isFirst());
        assertFalse(convertedPage.isLast());
      }
    }
  }
}