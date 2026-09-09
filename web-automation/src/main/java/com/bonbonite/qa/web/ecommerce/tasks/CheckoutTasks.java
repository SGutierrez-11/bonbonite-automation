package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.CommonActions.selectByVisibleText;
import static com.bonbonite.qa.web.actions.CommonActions.sendKeys;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_SUBTOTAL_LABEL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CART_TOTAL_LABEL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_ADDRESS;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_EMAIL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_FIRST_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_LAST_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_ORDER_REVIEW;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_PHONE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.CHECKOUT_POSTCODE;

import com.bonbonite.qa.api.models.request.CustomerRequest;
import com.bonbonite.qa.web.ecommerce.pages.CheckoutPage;
import io.qameta.allure.Step;

/**
 * Actions on the checkout screen.
 *
 * <p>This class deliberately offers no method that registers the order. The button
 * exists on the page and the automation verifies that it is available, but clicking
 * it would create a real order in the client's system, which is outside the agreed
 * scope of the project.</p>
 */
public class CheckoutTasks extends CheckoutPage {

  /**
   * Fills the shipping and billing data with the given customer.
   *
   * @param customer customer whose data is typed into the form
   */
  @Step("Diligenciar los datos de envío")
  public void fillShippingData(CustomerRequest customer) {
    scrollToElement(getTxtFirstName(), CHECKOUT_FIRST_NAME.getValue());
    sendKeys(getTxtFirstName(), customer.getFirstName(), CHECKOUT_FIRST_NAME.getValue());
    sendKeys(getTxtLastName(), customer.getLastName(), CHECKOUT_LAST_NAME.getValue());
    sendKeys(getTxtEmail(), customer.getEmail(), CHECKOUT_EMAIL.getValue());
    sendKeys(getTxtPhone(), customer.getPhone(), CHECKOUT_PHONE.getValue());
    sendKeys(getTxtAddress(), customer.getAddress(), CHECKOUT_ADDRESS.getValue());
    sendKeys(getTxtPostcode(), customer.getPostcode(), CHECKOUT_POSTCODE.getValue());
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
