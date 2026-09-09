package com.bonbonite.qa.web.ecommerce.stepdefinitions;

import static com.bonbonite.qa.api.context.TestContextManager.getTestContext;
import static com.bonbonite.qa.web.actions.WaitActions.isTheUrlContaining;
import static com.bonbonite.qa.web.driver.BrowserProperties.BASE_URL;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.SELECTED_SECTION;

import com.bonbonite.qa.web.ecommerce.enums.StoreSection;
import com.bonbonite.qa.web.ecommerce.questions.HeaderQuestions;
import com.bonbonite.qa.web.ecommerce.tasks.CookieBannerTasks;
import com.bonbonite.qa.web.ecommerce.tasks.HeaderTasks;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.testng.Assert;

/**
 * Steps for navigating the store.
 */
public class NavigationStepDefinitions extends WebBaseScreen {

  private static final int NAVIGATION_TIMEOUT = 20;

  private final CookieBannerTasks cookieBannerTasks = new CookieBannerTasks();
  private final HeaderTasks headerTasks = new HeaderTasks();
  private final HeaderQuestions headerQuestions = new HeaderQuestions();

  /**
   * Opens the store and dismisses the cookie banner to leave the interface clear.
   */
  @Dado("que ingreso a la tienda con las cookies rechazadas")
  public void openTheStoreRejectingCookies() {
    openPage(BASE_URL);
    cookieBannerTasks.rejectOptionalCookies();
  }

  /**
   * Navigates to a catalog section from the main menu.
   *
   * @param sectionName section name as it appears in the menu
   */
  @Cuando("navego a la sección {string}")
  public void navigateToSection(String sectionName) {
    StoreSection section = StoreSection.byMenuName(sectionName);
    getTestContext().set(SELECTED_SECTION.name(), section);
    headerTasks.navigateToSection(section);
  }

  /**
   * Verifies that the header offers every catalog section.
   */
  @Entonces("el encabezado muestra las secciones del catálogo")
  public void theHeaderShowsTheCatalogSections() {
    headerQuestions.verifyMainNavigationIsDisplayed();
  }

  /**
   * Verifies that the browser reached the listing of the selected section.
   */
  @Entonces("el sitio muestra el listado de esa sección")
  public void theStoreShowsTheSectionListing() {
    StoreSection section = getTestContext().get(SELECTED_SECTION.name());
    Assert.assertTrue(isTheUrlContaining(section.getCategorySlug(), NAVIGATION_TIMEOUT),
      "La URL no corresponde a la sección " + section.getMenuName()
        + ". URL actual: " + getCurrentUrl());
  }
}
