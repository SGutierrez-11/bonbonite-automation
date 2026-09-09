package com.bonbonite.qa.web.ecommerce.stepdefinitions;

import static com.bonbonite.qa.api.context.TestContextManager.getTestContext;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.CART_SUBTOTAL;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.PRODUCT_PRICE;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.SELECTED_PRODUCT;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.SELECTED_SIZE;

import com.bonbonite.qa.api.builders.CustomerBuilder;
import com.bonbonite.qa.api.models.response.store.ProductResponse;
import com.bonbonite.qa.api.tasks.store.ProductApiTask;
import com.bonbonite.qa.web.ecommerce.enums.StoreSection;
import com.bonbonite.qa.web.ecommerce.questions.PurchaseQuestions;
import com.bonbonite.qa.web.ecommerce.tasks.CartTasks;
import com.bonbonite.qa.web.ecommerce.tasks.CheckoutTasks;
import com.bonbonite.qa.web.ecommerce.tasks.ProductDetailTasks;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

/**
 * Steps of the purchase flow, from choosing a product to reviewing the order.
 *
 * <p>The flow never registers the order. Reaching a complete and valid summary is the
 * agreed limit, because the store creates a real order for the client's team to
 * follow up on.</p>
 */
public class PurchaseStepDefinitions extends WebBaseScreen {

  private final ProductDetailTasks productDetailTasks = new ProductDetailTasks();
  private final CartTasks cartTasks = new CartTasks();
  private final CheckoutTasks checkoutTasks = new CheckoutTasks();
  private final PurchaseQuestions purchaseQuestions = new PurchaseQuestions();

  /**
   * Resolves a product available for purchase through the catalog service and opens
   * its detail screen.
   *
   * <p>Asking the service instead of hardcoding a product is what keeps the scenario
   * stable: the catalog changes and stock runs out, and a fixed product would make
   * the test fail for reasons unrelated to the behaviour under test.</p>
   *
   * @param sectionName section of the catalog to pick the product from
   */
  @Dado("que abro un producto disponible de la sección {string}")
  public void openAnAvailableProductFromTheSection(String sectionName) {
    StoreSection section = StoreSection.byMenuName(sectionName);
    ProductResponse product =
      ProductApiTask.getFirstAvailableProductByCategory(section.getCategorySlug());
    getTestContext().set(SELECTED_PRODUCT.name(), product);
    openPage(product.getPermalink());
  }

  /**
   * Selects the first available size and adds the product to the cart.
   */
  @Cuando("agrego el producto al carrito en la primera talla disponible")
  public void addTheProductToTheCart() {
    getTestContext().set(PRODUCT_PRICE.name(), productDetailTasks.getProductPrice());
    getTestContext().set(SELECTED_SIZE.name(), productDetailTasks.selectFirstAvailableSize());
    productDetailTasks.addToCart();
  }

  /**
   * Verifies that the cart carries the chosen product and that the subtotal matches
   * its price.
   */
  @Entonces("el carrito refleja el producto y el subtotal correcto")
  public void theCartReflectsTheProductAndSubtotal() {
    cartTasks.openCart();
    ProductResponse product = getTestContext().get(SELECTED_PRODUCT.name());
    String subtotal = cartTasks.getSubtotal();
    getTestContext().set(CART_SUBTOTAL.name(), subtotal);
    purchaseQuestions.verifyCartContent(
      product.getName(), cartTasks.getFirstItemName(),
      getTestContext().get(PRODUCT_PRICE.name()), subtotal);
  }

  /**
   * Continues from the cart to the checkout and fills the shipping data.
   */
  @Cuando("continúo al checkout y diligencio los datos de envío")
  public void continueToCheckoutAndFillShippingData() {
    cartTasks.goToCheckout();
    checkoutTasks.fillShippingData(CustomerBuilder.randomCustomer());
  }

  /**
   * Verifies that the summary is complete and consistent, and that the store is ready
   * to register the order. The order itself is deliberately not registered.
   */
  @Entonces("el resumen de la orden está completo y listo para registrarse")
  public void theOrderSummaryIsCompleteAndReady() {
    purchaseQuestions.verifyOrderSummaryIsComplete();
    purchaseQuestions.verifyAmountsAreConsistent(
      getTestContext().get(CART_SUBTOTAL.name()),
      checkoutTasks.getSubtotal(),
      checkoutTasks.getTotal());
  }

  /**
   * Verifies that the product carried into the order summary is the chosen one.
   */
  @Y("el resumen conserva el producto seleccionado")
  public void theSummaryKeepsTheSelectedProduct() {
    ProductResponse product = getTestContext().get(SELECTED_PRODUCT.name());
    purchaseQuestions.verifyCartContent(
      product.getName(), checkoutTasks.getOrderProductName(),
      getTestContext().get(PRODUCT_PRICE.name()),
      checkoutTasks.getSubtotal());
  }
}
