package com.bonbonite.qa.api.utils;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.api.rest.Response;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Verificaciones comunes sobre las respuestas de los servicios.
 *
 * <p>Un servicio que no responde lo esperado es un problema de precondición, no un
 * defecto funcional del sitio. Por eso estas comprobaciones lanzan la excepción del
 * framework en lugar de fallar una aserción: el escenario se reporta como roto y no
 * como fallido, y quien lea el reporte distingue de inmediato un ambiente caído de
 * un error del producto.</p>
 */
@UtilityClass
public class ApiValidationUtils {

  /**
   * Verifica el código de estado y convierte el cuerpo en un objeto.
   *
   * @param response       respuesta a validar
   * @param type           clase destino de la conversión
   * @param expectedStatus código de estado esperado
   * @param <T>            tipo del objeto resultante
   * @return cuerpo de la respuesta convertido al tipo indicado
   * @throws CustomException si el código de estado no es el esperado
   */
  public static <T> T validateAndDeserialize(Response response, Class<T> type,
                                             int expectedStatus) {
    validateStatus(response, expectedStatus);
    return response.as(type);
  }

  /**
   * Verifica el código de estado y convierte el cuerpo en una lista de objetos.
   *
   * @param response       respuesta a validar
   * @param type           clase de los elementos de la lista
   * @param expectedStatus código de estado esperado
   * @param <T>            tipo de los elementos
   * @return cuerpo de la respuesta convertido en lista
   * @throws CustomException si el código de estado no es el esperado
   */
  public static <T> List<T> validateAndDeserializeList(Response response, Class<T> type,
                                                       int expectedStatus) {
    validateStatus(response, expectedStatus);
    return response.asList(type);
  }

  /**
   * Verifica que la respuesta tenga el código de estado esperado.
   *
   * @param response       respuesta a validar
   * @param expectedStatus código de estado esperado
   * @throws CustomException si el código de estado no coincide
   */
  public static void validateStatus(Response response, int expectedStatus) {
    if (response.getStatusCode() != expectedStatus) {
      throw new CustomException(String.format(
        "El servicio respondió con el estado %d y se esperaba %d. Cuerpo: %s",
        response.getStatusCode(), expectedStatus, response.getBodyAsString()));
    }
  }
}
