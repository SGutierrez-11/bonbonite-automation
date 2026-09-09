package com.bonbonite.qa.web.hooks;

import static com.bonbonite.qa.api.context.TestContextManager.cleanTestContext;

import com.bonbonite.qa.web.allure.AllureLogger;
import com.bonbonite.qa.web.assertions.SoftAssertManager;
import com.bonbonite.qa.web.driver.Browser;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

/**
 * Ciclo de vida de los escenarios web.
 *
 * <p>Los hooks solo gestionan recursos: levantan el navegador, consolidan las
 * aserciones acumuladas y limpian el contexto. Ninguna lógica de negocio vive aquí;
 * las precondiciones de datos se resuelven en los pasos, por servicio.</p>
 *
 * <p>El orden de los {@code @After} importa. Cucumber ejecuta primero los de mayor
 * {@code order}, así que las aserciones se consolidan antes de cerrar el navegador:
 * de lo contrario el listener de Allure no alcanzaría a capturar la pantalla del
 * fallo porque la sesión ya estaría cerrada.</p>
 */
@Slf4j
public class Hooks {

  /**
   * Registra en el reporte el entorno con el que se lanzó la suite.
   */
  @BeforeAll
  public static void reportEnvironment() {
    AllureLogger.writeEnvironmentInfo();
  }

  /**
   * Levanta el navegador antes de cada escenario etiquetado como web.
   *
   * @param scenario escenario que va a iniciar
   */
  @Before(value = "@web", order = 0)
  public void initDriver(Scenario scenario) {
    log.info("Iniciando escenario: {}", scenario.getName());
    WebBaseScreen.setDriver(Browser.createWebDriver());
  }

  /**
   * Consolida las aserciones suaves acumuladas durante el escenario.
   */
  @After(value = "@web", order = 2)
  public void assertAll() {
    SoftAssertManager.assertAll();
  }

  /**
   * Adjunta la evidencia final cuando el escenario termina en fallo.
   *
   * @param scenario escenario finalizado
   */
  @After(value = "@web", order = 1)
  public void attachEvidenceOnFailure(Scenario scenario) {
    if (scenario.isFailed()) {
      AllureLogger.attachScreenshot("Estado final del escenario");
      AllureLogger.attachPageSource();
    }
  }

  /**
   * Cierra el navegador y limpia el contexto al terminar el escenario.
   *
   * @param scenario escenario finalizado
   */
  @After(value = "@web", order = 0)
  public void tearDown(Scenario scenario) {
    log.info("Finalizando escenario: {} con estado {}", scenario.getName(), scenario.getStatus());
    WebBaseScreen.removeDriver();
    cleanTestContext();
  }
}
