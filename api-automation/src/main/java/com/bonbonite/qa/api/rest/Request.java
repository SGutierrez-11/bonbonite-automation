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
 * Wrapper over REST Assured that centralizes how requests are built.
 *
 * <p>It exists so services do not depend on the REST Assured API directly: the
 * filter that publishes every call into the Allure report, the masking of sensitive
 * headers and the console logging are applied once here. Changing the HTTP client or
 * the tracing policy is done in this class instead of in every service that uses
 * it.</p>
 */
public class Request {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  private final RequestSpecification specification;

  /**
   * Creates a request with the default filters and tracing of the framework.
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
   * Sets the base URI of the service.
   *
   * @param baseUri scheme and domain of the service
   * @return this request, for chaining
   */
  public Request baseUri(String baseUri) {
    specification.baseUri(baseUri);
    return this;
  }

  /**
   * Sets the path shared by every resource of the service.
   *
   * @param basePath path segment placed before the resource
   * @return this request, for chaining
   */
  public Request basePath(String basePath) {
    specification.basePath(basePath);
    return this;
  }

  /**
   * Sets the content type of the request.
   *
   * @param contentType content type to send
   * @return this request, for chaining
   */
  public Request contentType(ContentType contentType) {
    specification.contentType(contentType);
    return this;
  }

  /**
   * Adds a header.
   *
   * @param name  header name
   * @param value header value
   * @return this request, for chaining
   */
  public Request header(String name, String value) {
    specification.header(name, value);
    return this;
  }

  /**
   * Adds the authorization header with a bearer token.
   *
   * @param token access token
   * @return this request, for chaining
   */
  public Request bearerAuth(String token) {
    return header(AUTHORIZATION_HEADER, "Bearer " + token);
  }

  /**
   * Adds a query parameter.
   *
   * @param name  parameter name
   * @param value parameter value
   * @return this request, for chaining
   */
  public Request queryParam(String name, Object value) {
    specification.queryParam(name, value);
    return this;
  }

  /**
   * Adds several query parameters at once.
   *
   * @param params name value pairs to append to the URL
   * @return this request, for chaining
   */
  public Request queryParams(Map<String, ?> params) {
    specification.queryParams(params);
    return this;
  }

  /**
   * Adds a path parameter.
   *
   * @param name  placeholder name in the path
   * @param value value that replaces it
   * @return this request, for chaining
   */
  public Request pathParam(String name, Object value) {
    specification.pathParam(name, value);
    return this;
  }

  /**
   * Sets the request body. The object is serialized to JSON.
   *
   * @param body object to send as the body
   * @return this request, for chaining
   */
  public Request body(Object body) {
    specification.body(body);
    return this;
  }

  /**
   * Performs a GET call on the given resource.
   *
   * @param path resource path
   * @return the service response
   */
  public Response get(String path) {
    return send(Method.GET, path);
  }

  /**
   * Performs a POST call on the given resource.
   *
   * @param path resource path
   * @return the service response
   */
  public Response post(String path) {
    return send(Method.POST, path);
  }

  /**
   * Performs a PUT call on the given resource.
   *
   * @param path resource path
   * @return the service response
   */
  public Response put(String path) {
    return send(Method.PUT, path);
  }

  /**
   * Performs a PATCH call on the given resource.
   *
   * @param path resource path
   * @return the service response
   */
  public Response patch(String path) {
    return send(Method.PATCH, path);
  }

  /**
   * Performs a DELETE call on the given resource.
   *
   * @param path resource path
   * @return the service response
   */
  public Response delete(String path) {
    return send(Method.DELETE, path);
  }

  private Response send(Method method, String path) {
    return new Response(specification.request(method, path));
  }
}
