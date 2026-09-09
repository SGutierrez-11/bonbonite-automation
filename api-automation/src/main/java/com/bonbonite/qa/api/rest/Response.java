package com.bonbonite.qa.api.rest;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.List;
import lombok.Getter;

/**
 * Wrapper over the REST Assured response.
 *
 * <p>It exposes only what the framework needs — status code, body and
 * deserialization — and concentrates conversion error handling in a single place, so
 * that an unexpected contract produces a readable message instead of a library
 * stack trace.</p>
 */
@Getter
public class Response {

  private final io.restassured.response.Response response;

  /**
   * Wraps the response returned by the HTTP client.
   *
   * @param response original REST Assured response
   */
  public Response(io.restassured.response.Response response) {
    this.response = response;
  }

  /**
   * Returns the HTTP status code.
   *
   * @return the response status code
   */
  public int getStatusCode() {
    return response.getStatusCode();
  }

  /**
   * Returns the raw response body.
   *
   * @return the body as text
   */
  public String getBodyAsString() {
    return response.getBody().asString();
  }

  /**
   * Converts the response body into an object of the given type.
   *
   * @param type target class of the conversion
   * @param <T>  type of the resulting object
   * @return the object deserialized from the body
   * @throws CustomException if the body does not match the expected type
   */
  public <T> T as(Class<T> type) {
    try {
      return response.as(type);
    } catch (Exception exception) {
      throw new CustomException(
        "No fue posible convertir la respuesta a " + type.getSimpleName()
          + ". Cuerpo recibido: " + getBodyAsString(), exception);
    }
  }

  /**
   * Converts the response body into a list of objects of the given type.
   *
   * @param type class of the list elements
   * @param <T>  type of the elements
   * @return the list deserialized from the body
   * @throws CustomException if the body does not match a list of the expected type
   */
  public <T> List<T> asList(Class<T> type) {
    try {
      return response.jsonPath().getList("$", type);
    } catch (Exception exception) {
      throw new CustomException(
        "No fue posible convertir la respuesta a una lista de " + type.getSimpleName()
          + ". Cuerpo recibido: " + getBodyAsString(), exception);
    }
  }
}
