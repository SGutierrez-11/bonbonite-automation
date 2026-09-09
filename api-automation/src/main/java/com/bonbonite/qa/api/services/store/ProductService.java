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
 * Recursos públicos del catálogo de productos.
 *
 * <p>Los métodos de esta clase solo emiten la petición y devuelven la respuesta
 * sin interpretarla. La validación del código de estado y la conversión a objetos
 * del dominio son responsabilidad de la capa de tasks.</p>
 */
@UtilityClass
public class ProductService {

  /**
   * Consulta los productos del catálogo.
   *
   * @param perPage cantidad máxima de productos a devolver
   * @return respuesta del servicio
   */
  @Step("GET - /products")
  public static Response getProducts(int perPage) {
    return baseRequest()
      .queryParam("per_page", perPage)
      .get(PRODUCTS_PATH);
  }

  /**
   * Consulta los productos de una categoría.
   *
   * @param categorySlug identificador de la categoría en la URL
   * @param perPage      cantidad máxima de productos a devolver
   * @return respuesta del servicio
   */
  @Step("GET - /products?category={categorySlug}")
  public static Response getProductsByCategory(String categorySlug, int perPage) {
    return baseRequest()
      .queryParam("category", categorySlug)
      .queryParam("per_page", perPage)
      .get(PRODUCTS_PATH);
  }

  /**
   * Consulta un producto por su identificador.
   *
   * @param productId identificador del producto
   * @return respuesta del servicio
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
