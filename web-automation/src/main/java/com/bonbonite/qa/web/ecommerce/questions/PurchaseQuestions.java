package com.bonbonite.qa.web.ecommerce.questions;

import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.assertions.SoftAssertManager.getSoftAssert;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_ORDER_REVIEW;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_PAYMENT_METHOD;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_PLACE_ORDER_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_ENABLED;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_VISIBLE;

import com.bonbonite.qa.web.ecommerce.pages.CheckoutPage;
import io.qameta.allure.Step;
import java.util.Locale;

/**
 * Validations of the purchase flow, from the cart to the order summary.
 *
 * <p>Amounts are compared after stripping the currency symbol and the thousand
 * separators, because the store renders them as text and a direct string comparison
 * would fail on formatting alone.</p>
 */
public class PurchaseQuestions extends CheckoutPage {

  private static final int TIMEOUT = 20;

  /**
   * Verifies that the cart holds the expected product and that the amounts are
   * consistent with the price of that product.
   *
   * @param expectedProductName name of the product that was added
   * @param cartItemName        name shown on the cart line
   * @param productPrice        price shown on the product detail screen
   * @param cartSubtotal        subtotal shown by the cart
   */
  @Step("Verificar el contenido del carrito")
  public void verifyCartContent(String expectedProductName, String cartItemName,
                                String productPrice, String cartSubtotal) {
    getSoftAssert().assertTrue(
      normalizeText(cartItemName).contains(normalizeText(expectedProductName)),
      String.format("El carrito debe contener '%s' y muestra '%s'",
        expectedProductName, cartItemName));
    getSoftAssert().assertEquals(amountOf(cartSubtotal), amountOf(productPrice),
      "El subtotal del carrito debe corresponder al precio del producto");
  }

  /**
   * Verifies that the checkout offers everything the customer needs to complete the
   * order, without registering it.
   *
   * <p>The order button is asserted as available on purpose: reaching that state is
   * the agreed limit of the automation, because clicking it would create a real order
   * in the client's system.</p>
   *
   * <p>Shipping is not asserted: the store does not calculate it at checkout, it
   * announces that the cost will be informed before payment. There is no value to
   * verify there, and asserting its presence would only make the scenario brittle.</p>
   */
  @Step("Verificar el resumen de la orden")
  public void verifyOrderSummaryIsComplete() {
    getSoftAssert().assertTrue(isTheElementVisible(getLblOrderReview(), TIMEOUT),
      CHECKOUT_ORDER_REVIEW.getValue() + " " + IS_VISIBLE.getValue());
    getSoftAssert().assertTrue(isTheElementVisible(getLblPaymentMethod(), TIMEOUT),
      CHECKOUT_PAYMENT_METHOD.getValue() + " " + IS_VISIBLE.getValue());
    getSoftAssert().assertTrue(isTheElementVisible(getBtnPlaceOrder(), TIMEOUT),
      CHECKOUT_PLACE_ORDER_BUTTON.getValue() + " " + IS_VISIBLE.getValue());
    getSoftAssert().assertTrue(getBtnPlaceOrder().isEnabled(),
      CHECKOUT_PLACE_ORDER_BUTTON.getValue() + " " + IS_ENABLED.getValue());
  }

  /**
   * Verifies that the amounts carried into the checkout match the ones the cart
   * showed, so that no value changed between the two screens.
   *
   * @param cartSubtotal     subtotal shown by the cart
   * @param checkoutSubtotal subtotal shown by the checkout
   * @param checkoutTotal    total shown by the checkout
   */
  @Step("Verificar los totales de la orden")
  public void verifyAmountsAreConsistent(String cartSubtotal, String checkoutSubtotal,
                                         String checkoutTotal) {
    getSoftAssert().assertEquals(amountOf(checkoutSubtotal), amountOf(cartSubtotal),
      "El subtotal del checkout debe ser el mismo del carrito");
    getSoftAssert().assertTrue(amountOf(checkoutTotal) >= amountOf(checkoutSubtotal),
      "El total de la orden no puede ser menor que el subtotal");
  }


  /**
   * Turns a formatted amount into a number, dropping the currency symbol and the
   * thousand separators.
   *
   * @param formattedAmount amount as the store renders it, for instance {@code $597,900}
   * @return the numeric value of the amount
   */
  public static long amountOf(String formattedAmount) {
    String digits = formattedAmount == null ? "" : formattedAmount.replaceAll("[^0-9]", "");
    return digits.isEmpty() ? 0L : Long.parseLong(digits);
  }

  private static String normalizeText(String value) {
    return value == null ? "" : value.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
  }

}
