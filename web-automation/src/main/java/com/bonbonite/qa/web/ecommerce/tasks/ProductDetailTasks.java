package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.driver.BrowserProperties.WAIT_EXPLICIT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_ADD_TO_CART_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_PRICE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_SIZE_BUTTON;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.web.ecommerce.pages.ProductDetailPage;
import io.qameta.allure.Step;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Actions on the product detail screen.
 */
@Slf4j
public class ProductDetailTasks extends ProductDetailPage {

  /**
   * Selects a size from the buttons offered by the product.
   *
   * <p>The click updates a hidden field with the variation identifier through
   * JavaScript, so the task waits until that field carries a value before returning.
   * Otherwise the add to cart action could run against an unresolved variation.</p>
   *
   * @param size size to select, as displayed on the button
   * @throws CustomException if the product does not offer that size or the selection
   *                         never resolves into a variation
   */
  @Step("Seleccionar la talla {size}")
  public void selectSize(String size) {
    WebElement sizeButton = getLstSizeButtons().stream()
      .filter(button -> size.equals(button.getText().trim()))
      .findFirst()
      .orElseThrow(() -> new CustomException(String.format(
        "El producto no ofrece la talla '%s'. Tallas disponibles: %s", size, availableSizes())));

    scrollToElement(sizeButton, PRODUCT_SIZE_BUTTON.getValue());
    clickHandlingInterception(sizeButton, PRODUCT_SIZE_BUTTON.getValue() + " " + size);
    waitForVariationToResolve(size);
  }

  /**
   * Selects the first size the product offers.
   *
   * @return the size that was selected
   * @throws CustomException if the product offers no size
   */
  @Step("Seleccionar la primera talla disponible")
  public String selectFirstAvailableSize() {
    String size = getLstSizeButtons().stream()
      .map(button -> button.getText().trim())
      .filter(text -> !text.isEmpty())
      .findFirst()
      .orElseThrow(() -> new CustomException("El producto no ofrece ninguna talla seleccionable"));
    selectSize(size);
    return size;
  }

  /**
   * Adds the selected variation to the cart.
   */
  @Step("Añadir el producto al carrito")
  public void addToCart() {
    scrollToElement(getBtnAddToCart(), PRODUCT_ADD_TO_CART_BUTTON.getValue());
    clickHandlingInterception(getBtnAddToCart(), PRODUCT_ADD_TO_CART_BUTTON.getValue());
  }

  /**
   * Returns the product name shown on the screen.
   *
   * @return the product name
   */
  public String getProductName() {
    return getText(getLblProductName(), PRODUCT_NAME.getValue());
  }

  /**
   * Returns the product price shown on the screen.
   *
   * @return the price as displayed, including the currency symbol
   */
  public String getProductPrice() {
    return getText(getLblPrice(), PRODUCT_PRICE.getValue());
  }

  /**
   * Tells whether the add to cart button is available to the customer.
   *
   * @return {@code true} when the button is visible and enabled
   */
  public boolean isAddToCartAvailable() {
    return isTheElementVisible(getBtnAddToCart(), WAIT_EXPLICIT) && getBtnAddToCart().isEnabled();
  }

  private void waitForVariationToResolve(String size) {
    try {
      Awaitility.await()
        .atMost(Duration.ofSeconds(WAIT_EXPLICIT))
        .pollInterval(Duration.ofMillis(300))
        .until(() -> !getTxtVariationId().getDomProperty("value").isBlank());
    } catch (Exception exception) {
      throw new CustomException(String.format(
        "La talla '%s' se seleccionó pero el sitio no resolvió la variación del producto", size),
        exception);
    }
    log.info("Size {} selected, variation {}", size, getTxtVariationId().getDomProperty("value"));
  }

  private String availableSizes() {
    List<String> sizes = getLstSizeButtons().stream()
      .map(button -> button.getText().trim())
      .collect(Collectors.toList());
    return String.join(", ", sizes);
  }
}
