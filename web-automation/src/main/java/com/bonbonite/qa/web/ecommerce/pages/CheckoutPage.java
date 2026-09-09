package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Locators of the checkout screen.
 *
 * <p>The screen is where the automation stops. The order is never registered: the
 * button {@code #place_order} is located and validated as available, but the suite
 * does not click it, because doing so would create a real order in the client's
 * system. See the assumptions in the delivery document.</p>
 */
@Getter
public class CheckoutPage extends WebBaseScreen {

  @FindBy(css = "form.checkout")
  private WebElement frmCheckout;

  @FindBy(id = "billing_tipo_documento")
  private WebElement ddlDocumentType;

  @FindBy(id = "billing_user_login")
  private WebElement txtDocumentNumber;

  @FindBy(id = "billing_first_name")
  private WebElement txtFirstName;

  @FindBy(id = "billing_last_name")
  private WebElement txtLastName;

  @FindBy(id = "billing_email")
  private WebElement txtEmail;

  @FindBy(id = "billing_phone")
  private WebElement txtPhone;

  @FindBy(id = "billing_country")
  private WebElement ddlCountry;

  @FindBy(id = "billing_state")
  private WebElement ddlState;

  @FindBy(id = "billing_city")
  private WebElement ddlCity;

  @FindBy(id = "billing_address_1")
  private WebElement txtAddress;

  @FindBy(id = "billing_postcode")
  private WebElement txtPostcode;

  @FindBy(css = "#order_review, .woocommerce-checkout-review-order")
  private WebElement lblOrderReview;

  @FindBy(css = ".cart-subtotal .amount")
  private WebElement lblSubtotal;

  @FindBy(css = ".order-total .amount")
  private WebElement lblTotal;

  @FindBy(css = "#shipping_method")
  private WebElement lstShippingMethods;

  @FindBy(css = "ul.wc_payment_methods li")
  private WebElement lblPaymentMethod;

  @FindBy(id = "place_order")
  private WebElement btnPlaceOrder;

  @FindBy(css = ".woocommerce-checkout-review-order-table .product-name")
  private WebElement lblOrderProductName;
}
