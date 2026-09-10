package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Locators of the checkout screen.
 *
 * <p>The checkout is a three step wizard, not a single page: the cart summary, the
 * sign in step — skipped for an authenticated customer — and the billing details,
 * which is where the order summary and the order button live. Every step is present
 * in the document, but the inactive ones are rendered transparent, so they are found
 * and yet not visible until the wizard advances.</p>
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

  @FindBy(css = ".stepper-wrap button.resume-cta")
  private WebElement btnContinue;

  @FindBy(css = ".stepper-wrap .step")
  private List<WebElement> lstSteps;

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

  @FindBy(id = "order_review")
  private WebElement lblOrderReview;

  // Dos advertencias sobre estos localizadores. Primera: el tema no usa las clases
  // estandar de WooCommerce en el checkout, arma sus propias filas de totales; las
  // del carrito si son las estandar. Segunda: cada paso del asistente trae su propio
  // resumen, y el del paso inactivo queda transparente, de modo que todos se anclan
  // dentro de #order_review, que es el del paso de facturacion.
  @FindBy(css = "#order_review [data-title='Original Price'] .amount")
  private WebElement lblSubtotal;

  @FindBy(css = "#order_review .order-total-row .amount")
  private WebElement lblTotal;

  @FindBy(css = "#shipping_method")
  private WebElement lstShippingMethods;

  @FindBy(css = "#payment ul.wc_payment_methods li")
  private WebElement lblPaymentMethod;

  @FindBy(id = "place_order")
  private WebElement btnPlaceOrder;

  @FindBy(css = "#order_review .review-item-row .product-name")
  private WebElement lblOrderProductName;
}
