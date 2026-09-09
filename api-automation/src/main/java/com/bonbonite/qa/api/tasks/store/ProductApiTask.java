package com.bonbonite.qa.api.tasks.store;

import static com.bonbonite.qa.api.services.store.ProductService.getProducts;
import static com.bonbonite.qa.api.services.store.ProductService.getProductsByCategory;
import static com.bonbonite.qa.api.utils.ApiValidationUtils.validateAndDeserializeList;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.api.models.response.store.ProductResponse;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

/**
 * Business operations on the catalog.
 *
 * <p>These methods exist to resolve the preconditions of the web scenarios through
 * services. A purchase scenario must not carry a hardcoded product name: the catalog
 * changes and stock runs out, and a test that depends on a fixed value starts
 * failing for reasons unrelated to the defect it looks for. Asking the catalog what
 * is available keeps the scenario stable.</p>
 */
@Slf4j
@UtilityClass
public class ProductApiTask {

  private static final int DEFAULT_PAGE_SIZE = 20;

  /**
   * Returns the catalog products.
   *
   * @param perPage maximum number of products to query
   * @return the products returned by the service
   */
  public static List<ProductResponse> getCatalogProducts(int perPage) {
    return validateAndDeserializeList(
      getProducts(perPage), ProductResponse.class, HttpStatus.SC_OK);
  }

  /**
   * Returns the first catalog product available for purchase.
   *
   * @return a product in stock and enabled for sale
   * @throws CustomException if the catalog has no available product
   */
  public static ProductResponse getFirstAvailableProduct() {
    return firstAvailable(getCatalogProducts(DEFAULT_PAGE_SIZE), "el catálogo");
  }

  /**
   * Returns the first available product within a category.
   *
   * @param categorySlug category identifier used in the URL
   * @return a product in stock and enabled for sale
   * @throws CustomException if the category has no available product
   */
  public static ProductResponse getFirstAvailableProductByCategory(String categorySlug) {
    List<ProductResponse> products = validateAndDeserializeList(
      getProductsByCategory(categorySlug, DEFAULT_PAGE_SIZE),
      ProductResponse.class, HttpStatus.SC_OK);
    return firstAvailable(products, "la categoría '" + categorySlug + "'");
  }

  private static ProductResponse firstAvailable(List<ProductResponse> products, String origin) {
    ProductResponse product = products.stream()
      .filter(ProductResponse::isInStock)
      .filter(ProductResponse::isPurchasable)
      .findFirst()
      .orElseThrow(() -> new CustomException(
        "No se encontró ningún producto disponible para compra en " + origin));
    log.info("Product selected for the scenario: {} ({})", product.getName(), product.getId());
    return product;
  }
}
