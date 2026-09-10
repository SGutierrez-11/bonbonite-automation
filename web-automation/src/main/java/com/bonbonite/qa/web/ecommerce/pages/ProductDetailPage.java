package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Locators of the product detail screen.
 *
 * <p>Products are variable: the size is chosen through a grid of buttons that feed a
 * hidden select. The automation clicks the visible button, which is what a customer
 * does, and reads the resulting variation identifier from the hidden field to confirm
 * the selection actually registered.</p>
 *
 * <p>Choosing a size with no stock opens the reservation modal, which covers the
 * screen and swallows any following click. Its locators live here so the tasks can
 * dismiss it and carry on with another size.</p>
 */
@Getter
public class ProductDetailPage extends WebBaseScreen {

  @FindBy(css = "h1.product_title")
  private WebElement lblProductName;

  @FindBy(css = "p.price")
  private WebElement lblPrice;

  @FindBy(css = "form.variations_form")
  private WebElement frmAddToCart;

  @FindBy(css = "button.variation-button")
  private List<WebElement> lstSizeButtons;

  @FindBy(css = "select.variation-select")
  private WebElement ddlSize;

  @FindBy(css = "input.variation_id")
  private WebElement txtVariationId;

  @FindBy(css = "input.qty")
  private WebElement txtQuantity;

  @FindBy(css = ".woocommerce-variation-add-to-cart button[type='submit']")
  private WebElement btnAddToCart;

  @FindBy(css = ".woocommerce-variation-availability, .stock")
  private WebElement lblAvailability;

  @FindBy(id = "out-of-stock-modal")
  private WebElement lblOutOfStockModal;

  @FindBy(id = "close-modal")
  private WebElement btnCloseOutOfStockModal;
}
