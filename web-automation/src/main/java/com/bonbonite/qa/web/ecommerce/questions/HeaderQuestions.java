package com.bonbonite.qa.web.ecommerce.questions;

import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.assertions.SoftAssertManager.getSoftAssert;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_ACCESSORIES_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_ACCOUNT_LINK;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_BAGS_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_BELTS_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_CART_LINK;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_OUTLET_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_SHOES_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_VISIBLE;

import com.bonbonite.qa.web.ecommerce.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * Validaciones sobre el encabezado del sitio.
 */
public class HeaderQuestions extends HeaderComponent {

  private static final int TIMEOUT = 15;

  /**
   * Verifica que el encabezado ofrezca las cinco secciones del catálogo y los
   * accesos a la cuenta y al carrito.
   *
   * <p>Todas las comprobaciones son suaves: si falta más de un elemento, el reporte
   * los lista todos en lugar de detenerse en el primero.</p>
   */
  @Step("Verificar las opciones del encabezado")
  public void verifyMainNavigationIsDisplayed() {
    assertVisible(getLnkShoes(), HEADER_SHOES_MENU.getValue());
    assertVisible(getLnkBags(), HEADER_BAGS_MENU.getValue());
    assertVisible(getLnkBelts(), HEADER_BELTS_MENU.getValue());
    assertVisible(getLnkAccessories(), HEADER_ACCESSORIES_MENU.getValue());
    assertVisible(getLnkOutlet(), HEADER_OUTLET_MENU.getValue());
    assertVisible(getLnkAccount(), HEADER_ACCOUNT_LINK.getValue());
    assertVisible(getLnkCart(), HEADER_CART_LINK.getValue());
  }

  private void assertVisible(WebElement element, String description) {
    getSoftAssert().assertTrue(isTheElementVisible(element, TIMEOUT),
      description + " " + IS_VISIBLE.getValue());
  }
}
