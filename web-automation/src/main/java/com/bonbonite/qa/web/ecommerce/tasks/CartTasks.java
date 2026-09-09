package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.driver.BrowserProperties.CART_URL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_CHECKOUT_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_ITEM_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_SUBTOTAL_LABEL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_TOTAL_LABEL;

import com.bonbonite.qa.web.ecommerce.pages.CartPage;
import io.qameta.allure.Step;

/**
 * Actions on the cart screen.
 */
public class CartTasks extends CartPage {

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
}
