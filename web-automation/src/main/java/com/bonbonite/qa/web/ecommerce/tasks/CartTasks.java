package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.driver.BrowserProperties.CART_URL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_CHECKOUT_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_ITEM_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_SUBTOTAL_LABEL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_TOTAL_LABEL;

import com.bonbonite.qa.web.ecommerce.pages.CartPage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import io.qameta.allure.Step;

/**
 * Actions on the cart screen.
 */
@Slf4j
public class CartTasks extends CartPage {

  /** Upper bound of removals, so a cart that never empties cannot loop forever. */
  private static final int MAX_ITEMS_TO_REMOVE = 10;


  /**
   * Opens the cart directly by its address.
   */
  @Step("Abrir el carrito")
  public void openCart() {
    openPage(CART_URL);
  }

  /**
   * Continues from the cart to the checkout screen.
   */
  @Step("Continuar a finalizar compra")
  public void goToCheckout() {
    clickHandlingInterception(getBtnCheckout(), CART_CHECKOUT_BUTTON.getValue());
  }

  /**
   * Returns the name of the first product in the cart, including its size.
   *
   * @return the product name as displayed in the cart line
   */
  public String getFirstItemName() {
    return getText(getLblFirstItemName(), CART_ITEM_NAME.getValue());
  }

  /**
   * Returns the cart subtotal.
   *
   * @return the subtotal as displayed, including the currency symbol
   */
  public String getSubtotal() {
    return getText(getLblSubtotal(), CART_SUBTOTAL_LABEL.getValue());
  }

  /**
   * Returns the cart total.
   *
   * @return the total as displayed, including the currency symbol
   */
  public String getTotal() {
    return getText(getLblTotal(), CART_TOTAL_LABEL.getValue());
  }

  /**
   * Returns how many product lines the cart holds.
   *
   * @return the number of lines
   */
  public int getItemsCount() {
    return getLstCartItems().size();
  }

  /**
   * Removes every product from the cart.
   *
   * <p>The store keeps the cart tied to the customer account, so what a previous
   * execution left behind is still there on the next one. Starting from an empty cart
   * is what makes the amounts of the scenario predictable: otherwise the subtotal
   * carries products nobody added during the run.</p>
   */
  @Step("Vaciar el carrito")
  public void emptyCart() {
    openCart();
    for (int attempt = 0; attempt < MAX_ITEMS_TO_REMOVE; attempt++) {
      List<WebElement> removeLinks = getLstRemoveLinks();
      if (removeLinks.isEmpty()) {
        log.info("The cart is already empty");
        return;
      }
      String removeUrl = removeLinks.get(0).getDomAttribute("href");
      if (removeUrl == null || removeUrl.isBlank()) {
        return;
      }
      // El enlace de eliminar del tema esta oculto tras un control decorativo que
      // intercepta el clic, asi que se navega al recurso que el propio sitio expone.
      openPage(removeUrl);
    }
  }
}
