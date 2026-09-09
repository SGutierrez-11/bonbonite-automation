package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Punto de entrada de la suite.
 *
 * <p>El {@code glue} apunta al paquete raíz del proyecto para que Cucumber descubra
 * los hooks y los step definitions de todos los módulos de código, sin tener que
 * enumerarlos uno por uno cuando el monorepo crezca.</p>
 *
 * <p>Sobrescribir el proveedor de datos con {@code parallel = true} es lo que habilita
 * la ejecución de escenarios en paralelo. El número de hilos lo controla Surefire con
 * la propiedad {@code threadCount}.</p>
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
