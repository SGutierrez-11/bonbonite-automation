package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementInvisible;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.driver.BrowserProperties.WAIT_EXPLICIT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_ADD_TO_CART_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.OUT_OF_STOCK_MODAL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_PRICE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PRODUCT_SIZE_BUTTON;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.web.ecommerce.pages.ProductDetailPage;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebElement;

/**
 * Actions on the product detail screen.
 */
@Slf4j
public class ProductDetailTasks extends ProductDetailPage {

  /** Seconds allowed for the reservation modal to appear or disappear. */
  private static final int MODAL_TIMEOUT = 5;

  /** Marks the store puts on the button when the chosen variation cannot be sold. */
  private static final List<String> UNAVAILABLE_MARKS =
    List.of("disabled", "wc-variation-is-unavailable");

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
        "El producto no ofrece la talla '%s'. Tallas disponibles: %s", size, offeredSizes())));

    scrollToElement(sizeButton, PRODUCT_SIZE_BUTTON.getValue());
    clickHandlingInterception(sizeButton, PRODUCT_SIZE_BUTTON.getValue() + " " + size);
    waitForVariationToResolve(size);
  }

  /**
   * Selects the first size that the store can actually sell.
   *
   * <p>The product offers a button for every size of the model, including the ones
   * with no stock. Choosing one of those leaves the add to cart button disabled and
   * opens a modal that swallows the next click, so the task checks the state of the
   * button after each selection and moves on to the next size until it finds one that
   * is purchasable. That is also what a customer does.</p>
   *
   * @return the size that was selected
   * @throws CustomException if no size of the product can be purchased
   */
  @Step("Seleccionar la primera talla con existencias")
  public String selectFirstPurchasableSize() {
    List<String> offered = offeredSizesList();
    if (offered.isEmpty()) {
      throw new CustomException("El producto no ofrece ninguna talla seleccionable");
    }

    for (String size : offered) {
      selectSize(size);
      if (isAddToCartAvailable()) {
        log.info("Size {} is purchasable", size);
        return size;
      }
      log.info("Size {} has no stock, trying the next one", size);
      dismissOutOfStockModal();
    }

    throw new CustomException(String.format(
      "Ninguna de las tallas del producto tiene existencias. Tallas ofrecidas: %s",
      String.join(", ", offered)));
  }

  /**
   * Adds the selected variation to the cart.
   *
   * @throws CustomException if the selected variation cannot be purchased
   */
  @Step("Añadir el producto al carrito")
  public void addToCart() {
    if (!isAddToCartAvailable()) {
      throw new CustomException(
        "La variación seleccionada no está disponible para compra, el botón sigue deshabilitado");
    }
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
   * Tells whether the store can sell the variation that is currently selected.
   *
   * <p>Being visible and enabled is not enough: the store keeps the button in the
   * page and marks it with its own classes when the variation has no stock.</p>
   *
   * @return {@code true} when the button is ready to add the product to the cart
   */
  public boolean isAddToCartAvailable() {
    if (!isTheElementVisible(getBtnAddToCart(), WAIT_EXPLICIT)) {
      return false;
    }
    if (!getBtnAddToCart().isEnabled()) {
      return false;
    }
    String classes = String.valueOf(getBtnAddToCart().getDomAttribute("class")).toLowerCase();
    return UNAVAILABLE_MARKS.stream().noneMatch(classes::contains);
  }

  /**
   * Closes the reservation modal the store opens when the chosen size has no stock.
   *
   * <p>The modal covers the whole screen, so leaving it open makes every following
   * click land on it instead of on the page. It does nothing when the modal is not
   * showing.</p>
   */
  @Step("Cerrar el aviso de producto sin existencias")
  public void dismissOutOfStockModal() {
    if (!isTheElementVisible(getLblOutOfStockModal(), MODAL_TIMEOUT)) {
      return;
    }
    clickHandlingInterception(getBtnCloseOutOfStockModal(), OUT_OF_STOCK_MODAL.getValue());
    isTheElementInvisible(getLblOutOfStockModal(), MODAL_TIMEOUT);
  }

  private void waitForVariationToResolve(String size) {
    try {
      Awaitility.await()
        .atMost(Duration.ofSeconds(WAIT_EXPLICIT))
        .pollInterval(Duration.ofMillis(300))
        .until(() -> !String.valueOf(getTxtVariationId().getDomProperty("value")).isBlank());
    } catch (Exception exception) {
      throw new CustomException(String.format(
        "La talla '%s' se seleccionó pero el sitio no resolvió la variación del producto", size),
        exception);
    }
  }

  private List<String> offeredSizesList() {
    return getLstSizeButtons().stream()
      .map(button -> button.getText().trim())
      .filter(text -> !text.isEmpty())
      .collect(Collectors.toList());
  }

  private String offeredSizes() {
    return String.join(", ", offeredSizesList());
  }
}
