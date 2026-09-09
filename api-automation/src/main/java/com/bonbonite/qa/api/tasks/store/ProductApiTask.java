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
 * Operaciones de negocio sobre el catálogo.
 *
 * <p>Estos métodos existen para resolver por servicio las precondiciones de los
 * escenarios web. Un escenario de compra no debe traer quemado el nombre de un
 * producto: el catálogo cambia y las existencias se agotan, y una prueba que
 * dependa de un dato fijo empieza a fallar por motivos ajenos al defecto que busca.
 * Preguntarle al catálogo qué hay disponible mantiene el escenario estable.</p>
 */
@Slf4j
@UtilityClass
public class ProductApiTask {

  private static final int DEFAULT_PAGE_SIZE = 20;

  /**
   * Obtiene los productos del catálogo.
   *
   * @param perPage cantidad máxima de productos a consultar
   * @return productos devueltos por el servicio
   */
  public static List<ProductResponse> getCatalogProducts(int perPage) {
    return validateAndDeserializeList(
      getProducts(perPage), ProductResponse.class, HttpStatus.SC_OK);
  }

  /**
   * Obtiene el primer producto del catálogo que esté disponible para compra.
   *
   * @return producto con existencias y habilitado para la venta
   * @throws CustomException si el catálogo no tiene ningún producto disponible
   */
  public static ProductResponse getFirstAvailableProduct() {
    return firstAvailable(getCatalogProducts(DEFAULT_PAGE_SIZE), "el catálogo");
  }

  /**
   * Obtiene el primer producto disponible dentro de una categoría.
   *
   * @param categorySlug identificador de la categoría en la URL
   * @return producto con existencias y habilitado para la venta
   * @throws CustomException si la categoría no tiene ningún producto disponible
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
    log.info("Producto seleccionado para el escenario: {} ({})", product.getName(), product.getId());
    return product;
  }
}
