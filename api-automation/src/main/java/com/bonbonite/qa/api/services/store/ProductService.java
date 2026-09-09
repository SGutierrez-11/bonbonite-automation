package com.bonbonite.qa.api.services.store;

import static com.bonbonite.qa.api.constants.ServiceConstants.PRODUCTS_PATH;
import static com.bonbonite.qa.api.constants.ServiceConstants.STORE_BASE_PATH;
import static com.bonbonite.qa.api.constants.ServiceConstants.STORE_BASE_URI;

import com.bonbonite.qa.api.rest.Request;
import com.bonbonite.qa.api.rest.Response;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.experimental.UtilityClass;

/**
 * Public resources of the product catalog.
 *
 * <p>Methods in this class only send the request and return the response without
 * interpreting it. Validating the status code and converting the body into domain
 * objects belongs to the task layer.</p>
 */
@UtilityClass
public class ProductService {

  /**
   * Queries the catalog products.
   *
   * @param perPage maximum number of products to return
   * @return the service response
   */
  @Step("GET - /products")
  public static Response getProducts(int perPage) {
    return baseRequest()
      .queryParam("per_page", perPage)
      .get(PRODUCTS_PATH);
  }

  /**
   * Queries the products of a category.
   *
   * @param categorySlug category identifier used in the URL
   * @param perPage      maximum number of products to return
   * @return the service response
   */
  @Step("GET - /products?category={categorySlug}")
  public static Response getProductsByCategory(String categorySlug, int perPage) {
    return baseRequest()
      .queryParam("category", categorySlug)
      .queryParam("per_page", perPage)
      .get(PRODUCTS_PATH);
  }

  /**
   * Queries a product by its identifier.
   *
   * @param productId product identifier
   * @return the service response
   */
  @Step("GET - /products/{productId}")
  public static Response getProductById(long productId) {
    return baseRequest().get(PRODUCTS_PATH + "/" + productId);
  }

  private static Request baseRequest() {
    return new Request()
      .baseUri(STORE_BASE_URI)
      .basePath(STORE_BASE_PATH)
      .contentType(ContentType.JSON);
  }
}
