package com.bonbonite.qa.api.rest;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.List;
import lombok.Getter;

/**
 * Envoltorio sobre la respuesta de REST Assured.
 *
 * <p>Expone únicamente lo que el framework necesita —código de estado, cuerpo y
 * deserialización— y concentra el manejo de errores de conversión en un solo lugar,
 * de modo que un contrato inesperado produzca un mensaje entendible en vez de una
 * excepción de la librería.</p>
 */
@Getter
public class Response {

  private final io.restassured.response.Response response;

  /**
   * Envuelve la respuesta devuelta por el cliente HTTP.
   *
   * @param response respuesta original de REST Assured
   */
  public Response(io.restassured.response.Response response) {
    this.response = response;
  }

  /**
   * Devuelve el código de estado HTTP.
   *
   * @return código de estado de la respuesta
   */
  public int getStatusCode() {
    return response.getStatusCode();
  }

  /**
   * Devuelve el cuerpo de la respuesta sin procesar.
   *
   * @return cuerpo como cadena de texto
   */
  public String getBodyAsString() {
    return response.getBody().asString();
  }

  /**
   * Convierte el cuerpo de la respuesta en un objeto del tipo indicado.
   *
   * @param type clase destino de la conversión
   * @param <T>  tipo del objeto resultante
   * @return objeto deserializado a partir del cuerpo
   * @throws CustomException si el cuerpo no corresponde al tipo esperado
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
   * Convierte el cuerpo de la respuesta en una lista de objetos del tipo indicado.
   *
   * @param type clase de los elementos de la lista
   * @param <T>  tipo de los elementos
   * @return lista deserializada a partir del cuerpo
   * @throws CustomException si el cuerpo no corresponde a una lista del tipo esperado
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
