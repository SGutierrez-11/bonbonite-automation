package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Entry point of the suite.
 *
 * <p>The glue points at the project root package so Cucumber discovers the hooks and
 * step definitions of every code module, without having to list them one by one as
 * the monorepo grows.</p>
 *
 * <p>Overriding the data provider with {@code parallel = true} is what enables
 * running scenarios concurrently. The number of threads is controlled by Surefire
 * through the {@code threadCount} property.</p>
 */
@CucumberOptions(
  features = "src/test/resources/features",
  glue = {"com.bonbonite.qa"},
  snippets = CucumberOptions.SnippetType.CAMELCASE,
  plugin = {
    "pretty",
    "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
    "html:target/cucumber/cucumber-pretty.html",
    "json:target/cucumber/cucumber.json"
  }
)
public class TestRunner extends AbstractTestNGCucumberTests {

  @Override
  @DataProvider(parallel = true)
  public Object[][] scenarios() {
    return super.scenarios();
  }
}
