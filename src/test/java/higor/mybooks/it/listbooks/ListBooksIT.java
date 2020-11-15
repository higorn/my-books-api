package higor.mybooks.it.listbooks;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/ListBooks.feature",
  plugin = {"summary", "pretty", "html:build/cucumber-report/", "json:build/cucumber_ListBooks.json"},
  tags = "not @ignore")
public class ListBooksIT {
  @Test
  public void dummyTest() {
  }
}
