package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Locators of the cart screen.
 */
@Getter
public class CartPage extends WebBaseScreen {

  @FindBy(css = "tr.woocommerce-cart-form__cart-item")
  private List<WebElement> lstCartItems;

  @FindBy(css = "tr.woocommerce-cart-form__cart-item td.product-name")
  private WebElement lblFirstItemName;

  @FindBy(css = "tr.woocommerce-cart-form__cart-item input.qty")
  private WebElement txtFirstItemQuantity;

  @FindBy(css = ".cart-subtotal .amount")
  private WebElement lblSubtotal;

  @FindBy(css = ".order-total .amount")
  private WebElement lblTotal;

  @FindBy(css = "a[href*='finalizar-compra']")
  private WebElement btnCheckout;

  @FindBy(css = "td.product-remove a")
  private WebElement btnRemoveFirstItem;

  @FindBy(css = "a.remove[href*='remove_item']")
  private List<WebElement> lstRemoveLinks;

  @FindBy(css = ".cart-empty, .woocommerce-cart-form")
  private WebElement lblCartState;
}
