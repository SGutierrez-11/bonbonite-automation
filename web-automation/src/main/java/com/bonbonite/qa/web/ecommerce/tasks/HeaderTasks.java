package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_ACCESSORIES_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_ACCOUNT_LINK;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_BAGS_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_BELTS_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_CART_COUNT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_CART_LINK;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_OUTLET_MENU;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.HEADER_SHOES_MENU;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.web.ecommerce.enums.StoreSection;
import com.bonbonite.qa.web.ecommerce.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * Navegación desde el encabezado del sitio.
 */
public class HeaderTasks extends HeaderComponent {

  private static final int COUNTER_TIMEOUT = 5;

  /**
   * Navega a una de las secciones del catálogo desde el menú principal.
   *
   * @param section sección a abrir
   */
  @Step("Navegar a la sección {section}")
  public void navigateToSection(StoreSection section) {
    clickHandlingInterception(linkOf(section), descriptionOf(section));
  }

  /**
   * Abre la pantalla de cuenta desde el encabezado.
   */
  @Step("Abrir mi cuenta")
  public void openAccount() {
    clickHandlingInterception(getLnkAccount(), HEADER_ACCOUNT_LINK.getValue());
  }

  /**
   * Abre el carrito desde el encabezado.
   */
  @Step("Abrir el carrito")
  public void openCart() {
    clickHandlingInterception(getLnkCart(), HEADER_CART_LINK.getValue());
  }

  /**
   * Devuelve la cantidad de productos que indica el encabezado.
   *
   * <p>El sitio oculta la insignia del contador mientras el carrito está vacío, de
   * modo que su ausencia no es un error sino la representación de cero productos.</p>
   *
   * @return número de productos en el carrito
   */
  @Step("Consultar la cantidad de productos del carrito")
  public int getCartItemsCount() {
    if (!isTheElementVisible(getLblCartCount(), COUNTER_TIMEOUT)) {
      return 0;
    }
    return Integer.parseInt(getText(getLblCartCount(), HEADER_CART_COUNT.getValue()));
  }

  private WebElement linkOf(StoreSection section) {
    return switch (section) {
      case SHOES -> getLnkShoes();
      case BAGS -> getLnkBags();
      case BELTS -> getLnkBelts();
      case ACCESSORIES -> getLnkAccessories();
      case OUTLET -> getLnkOutlet();
      default -> throw new CustomException(
        "La sección " + section + " no está disponible en el menú principal");
    };
  }

  private String descriptionOf(StoreSection section) {
    return switch (section) {
      case SHOES -> HEADER_SHOES_MENU.getValue();
      case BAGS -> HEADER_BAGS_MENU.getValue();
      case BELTS -> HEADER_BELTS_MENU.getValue();
      case ACCESSORIES -> HEADER_ACCESSORIES_MENU.getValue();
      case OUTLET -> HEADER_OUTLET_MENU.getValue();
      default -> section.name();
    };
  }
}
