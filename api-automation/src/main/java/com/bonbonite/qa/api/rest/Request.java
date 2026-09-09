package com.bonbonite.qa.api.rest;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

/**
 * Envoltorio sobre REST Assured que centraliza la construcción de peticiones.
 *
 * <p>Existe para que los servicios no dependan directamente de la API de REST
 * Assured: aquí se aplican de una sola vez el filtro que publica cada petición en
 * el reporte de Allure, el enmascaramiento de las cabeceras sensibles y el registro
 * en consola. Cambiar el cliente HTTP o la política de trazas se hace en esta clase
 * y no en las decenas de servicios que la usan.</p>
 */
public class Request {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  private final RequestSpecification specification;

  /**
   * Crea una petición con las trazas y filtros por defecto del framework.
   */
  public Request() {
    LogConfig logConfig = LogConfig.logConfig()
      .blacklistHeader(AUTHORIZATION_HEADER)
      .blacklistHeader("X-API-KEY")
      .blacklistHeader("Cookie");

    this.specification = RestAssured.given()
      .config(RestAssuredConfig.config().logConfig(logConfig))
      .filter(new AllureRestAssured())
      .filter(new RequestLoggingFilter(LogDetail.URI))
      .filter(new ResponseLoggingFilter(LogDetail.STATUS))
      .relaxedHTTPSValidation();
  }

  /**
   * Define la URI base del servicio.
   *
   * @param baseUri esquema y dominio del servicio
   * @return la misma petición, para encadenar
   */
  public Request baseUri(String baseUri) {
    specification.baseUri(baseUri);
    return this;
  }

  /**
   * Define la ruta común que antecede al recurso.
   *
   * @param basePath segmento de ruta previo al recurso
   * @return la misma petición, para encadenar
   */
  public Request basePath(String basePath) {
    specification.basePath(basePath);
    return this;
  }

  /**
   * Define el tipo de contenido de la petición.
   *
   * @param contentType tipo de contenido
   * @return la misma petición, para encadenar
   */
  public Request contentType(ContentType contentType) {
    specification.contentType(contentType);
    return this;
  }

  /**
   * Agrega una cabecera.
   *
   * @param name  nombre de la cabecera
   * @param value valor de la cabecera
   * @return la misma petición, para encadenar
   */
  public Request header(String name, String value) {
    specification.header(name, value);
    return this;
  }

  /**
   * Agrega la cabecera de autorización con un token de tipo bearer.
   *
   * @param token token de acceso
   * @return la misma petición, para encadenar
   */
  public Request bearerAuth(String token) {
    return header(AUTHORIZATION_HEADER, "Bearer " + token);
  }

  /**
   * Agrega un parámetro de consulta.
   *
   * @param name  nombre del parámetro
   * @param value valor del parámetro
   * @return la misma petición, para encadenar
   */
  public Request queryParam(String name, Object value) {
    specification.queryParam(name, value);
    return this;
  }

  /**
   * Agrega varios parámetros de consulta.
   *
   * @param params pares nombre-valor a incluir en la URL
   * @return la misma petición, para encadenar
   */
  public Request queryParams(Map<String, ?> params) {
    specification.queryParams(params);
    return this;
  }

  /**
   * Agrega un parámetro de ruta.
   *
   * @param name  nombre del marcador en la ruta
   * @param value valor que lo reemplaza
   * @return la misma petición, para encadenar
   */
  public Request pathParam(String name, Object value) {
    specification.pathParam(name, value);
    return this;
  }

  /**
   * Define el cuerpo de la petición. El objeto se serializa a JSON.
   *
   * @param body objeto a enviar como cuerpo
   * @return la misma petición, para encadenar
   */
  public Request body(Object body) {
    specification.body(body);
    return this;
  }

  /**
   * Ejecuta una petición GET sobre el recurso indicado.
   *
   * @param path ruta del recurso
   * @return respuesta del servicio
   */
  public Response get(String path) {
    return send(Method.GET, path);
  }

  /**
   * Ejecuta una petición POST sobre el recurso indicado.
   *
   * @param path ruta del recurso
   * @return respuesta del servicio
   */
  public Response post(String path) {
    return send(Method.POST, path);
  }

  /**
   * Ejecuta una petición PUT sobre el recurso indicado.
   *
   * @param path ruta del recurso
   * @return respuesta del servicio
   */
  public Response put(String path) {
    return send(Method.PUT, path);
  }

  /**
   * Ejecuta una petición PATCH sobre el recurso indicado.
   *
   * @param path ruta del recurso
   * @return respuesta del servicio
   */
  public Response patch(String path) {
    return send(Method.PATCH, path);
  }

  /**
   * Ejecuta una petición DELETE sobre el recurso indicado.
   *
   * @param path ruta del recurso
   * @return respuesta del servicio
   */
  public Response delete(String path) {
    return send(Method.DELETE, path);
  }

  private Response send(Method method, String path) {
    return new Response(specification.request(method, path));
  }
}
