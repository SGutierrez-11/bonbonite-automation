package com.bonbonite.qa.api.utils;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.api.rest.Response;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Common checks on service responses.
 *
 * <p>A service that does not answer as expected is a precondition problem, not a
 * functional defect of the site. That is why these checks throw the framework
 * exception instead of failing an assertion: the scenario is reported as broken
 * rather than failed, and whoever reads the report immediately tells a down
 * environment apart from a product error.</p>
 */
@UtilityClass
public class ApiValidationUtils {

  /**
   * Validates the status code and converts the body into an object.
   *
   * @param response       response to validate
   * @param type           target class of the conversion
   * @param expectedStatus expected status code
   * @param <T>            type of the resulting object
   * @return the response body converted into the given type
   * @throws CustomException if the status code is not the expected one
   */
  public static <T> T validateAndDeserialize(Response response, Class<T> type,
                                             int expectedStatus) {
    validateStatus(response, expectedStatus);
    return response.as(type);
  }

  /**
   * Validates the status code and converts the body into a list of objects.
   *
   * @param response       response to validate
   * @param type           class of the list elements
   * @param expectedStatus expected status code
   * @param <T>            type of the elements
   * @return the response body converted into a list
   * @throws CustomException if the status code is not the expected one
   */
  public static <T> List<T> validateAndDeserializeList(Response response, Class<T> type,
                                                       int expectedStatus) {
    validateStatus(response, expectedStatus);
    return response.asList(type);
  }

  /**
   * Validates that the response carries the expected status code.
   *
   * @param response       response to validate
   * @param expectedStatus expected status code
   * @throws CustomException if the status code does not match
   */
  public static void validateStatus(Response response, int expectedStatus) {
    if (response.getStatusCode() != expectedStatus) {
      throw new CustomException(String.format(
        "El servicio respondió con el estado %d y se esperaba %d. Cuerpo: %s",
        response.getStatusCode(), expectedStatus, response.getBodyAsString()));
    }
  }
}
