package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.CommonActions.selectByVisibleText;
import static com.bonbonite.qa.web.actions.CommonActions.sendKeys;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.actions.WaitActions.isTheUrlContaining;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_SUBTOTAL_LABEL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_TOTAL_LABEL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_ADDRESS;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_CONTINUE_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_EMAIL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_FIRST_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_LAST_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_ORDER_REVIEW;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_PHONE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_POSTCODE;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.api.models.request.CustomerRequest;
import com.bonbonite.qa.web.ecommerce.pages.CheckoutPage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Actions on the checkout screen.
 *
 * <p>This class deliberately offers no method that registers the order. The button
 * exists on the page and the automation verifies that it is available, but clicking
 * it would create a real order in the client's system, which is outside the agreed
 * scope of the project.</p>
 */
@Slf4j
public class CheckoutTasks extends CheckoutPage {

  /** Seconds allowed to decide whether the store is asking for shipping data. */
  private static final int FORM_TIMEOUT = 10;

  /** Seconds allowed for the order summary to finish rendering. */
  private static final int SUMMARY_TIMEOUT = 60;

  /** Fragment that identifies the checkout address. */
  private static final String CHECKOUT_PATH = "finalizar-compra";

  /** Steps the wizard can hold; the loop never presses continue more times. */
  private static final int MAX_WIZARD_STEPS = 4;

  /** Seconds allowed for a step of the wizard to become active. */
  private static final int STEP_TIMEOUT = 15;

  /**
   * Fills the shipping and billing data when the store asks for it.
   *
   * <p>An authenticated customer whose account already carries an address does not
   * see these fields: the store reuses the saved data and renders the summary
   * directly. Forcing the fields to be there would make the scenario fail for a
   * customer who is, in fact, further along in the flow. The task therefore fills
   * them only when the store presents them.</p>
   *
   * @param customer customer whose data is typed into the form
   * @return {@code true} when the form was presented and filled
   */
  @Step("Diligenciar los datos de envío")
  public boolean fillShippingData(CustomerRequest customer) {
    if (!isTheElementVisible(getTxtFirstName(), FORM_TIMEOUT)) {
      log.info("The checkout did not ask for shipping data; the account already carries it");
      return false;
    }
    scrollToElement(getTxtFirstName(), CHECKOUT_FIRST_NAME.getValue());
    sendKeys(getTxtFirstName(), customer.getFirstName(), CHECKOUT_FIRST_NAME.getValue());
    sendKeys(getTxtLastName(), customer.getLastName(), CHECKOUT_LAST_NAME.getValue());
    sendKeys(getTxtEmail(), customer.getEmail(), CHECKOUT_EMAIL.getValue());
    sendKeys(getTxtPhone(), customer.getPhone(), CHECKOUT_PHONE.getValue());
    sendKeys(getTxtAddress(), customer.getAddress(), CHECKOUT_ADDRESS.getValue());
    sendKeys(getTxtPostcode(), customer.getPostcode(), CHECKOUT_POSTCODE.getValue());
    return true;
  }

  /**
   * Waits until the browser reached the checkout and its order summary rendered.
   *
   * <p>Waiting for the address first matters: querying an element while the browser
   * is still navigating searches the previous document, and the search would expire
   * against a page that was already gone.</p>
   *
   * @throws CustomException if the checkout never loads or the summary never appears
   */
  @Step("Esperar el resumen de la orden")
  public void awaitOrderSummary() {
    if (!isTheUrlContaining(CHECKOUT_PATH, SUMMARY_TIMEOUT)) {
      throw new CustomException(String.format(
        "El navegador no llegó a la pantalla de finalizar compra. URL actual: %s",
        getCurrentUrl()));
    }
    advanceToBillingStep();
    if (!isTheElementVisible(getLblOrderReview(), SUMMARY_TIMEOUT)) {
      throw new CustomException(String.format(
        "El resumen de la orden no se mostró después de %d segundos en %s",
        SUMMARY_TIMEOUT, getCurrentUrl()));
    }
  }

  /**
   * Advances the wizard until the billing step, which is the one holding the order
   * summary.
   *
   * <p>The store lays the checkout out as three steps inside the same document: the
   * cart summary, the sign in — already resolved for an authenticated customer — and
   * the billing details. Each press of the continue button moves one step forward, so
   * the task presses it until the summary becomes visible.</p>
   */
  @Step("Avanzar hasta el paso de facturación")
  public void advanceToBillingStep() {
    for (int step = 0; step < MAX_WIZARD_STEPS; step++) {
      if (isTheElementVisible(getLblOrderReview(), STEP_TIMEOUT)) {
        return;
      }
      if (!isTheElementVisible(getBtnContinue(), STEP_TIMEOUT)) {
        return;
      }
      scrollToElement(getBtnContinue(), CHECKOUT_CONTINUE_BUTTON.getValue());
      clickHandlingInterception(getBtnContinue(), CHECKOUT_CONTINUE_BUTTON.getValue());
    }
  }

  /**
   * Selects a department in the shipping form.
   *
   * @param state department to select, as displayed in the list
   */
  @Step("Seleccionar el departamento {state}")
  public void selectState(String state) {
    selectByVisibleText(getDdlState(), state, "Departamento");
  }

  /**
   * Returns the subtotal shown in the order summary.
   *
   * @return the subtotal as displayed, including the currency symbol
   */
  public String getSubtotal() {
    return getText(getLblSubtotal(), CART_SUBTOTAL_LABEL.getValue());
  }

  /**
   * Returns the total shown in the order summary.
   *
   * @return the total as displayed, including the currency symbol
   */
  public String getTotal() {
    return getText(getLblTotal(), CART_TOTAL_LABEL.getValue());
  }

  /**
   * Returns the product listed in the order summary.
   *
   * @return the product name of the summary line
   */
  public String getOrderProductName() {
    return getText(getLblOrderProductName(), CHECKOUT_ORDER_REVIEW.getValue());
  }
}
