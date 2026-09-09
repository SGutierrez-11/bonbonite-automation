package com.bonbonite.qa.api.constants;

import static com.bonbonite.qa.api.config.PropertiesManager.getParameter;

import lombok.experimental.UtilityClass;

/**
 * Base addresses of the services consumed by the automation.
 *
 * <p>Values are resolved once when the class is loaded, with the precedence defined
 * in the properties manager, so pointing at another environment is only a matter of
 * changing an execution parameter.</p>
 */
@UtilityClass
public class ServiceConstants {

  private static final String API_MODULE = "api";

  /** Scheme and domain of the store. */
  public static final String STORE_BASE_URI = getParameter("store.base.uri", API_MODULE);

  /** Shared path of the public Store API resources. */
  public static final String STORE_BASE_PATH = getParameter("store.base.path", API_MODULE);

  /** Catalog products resource. */
  public static final String PRODUCTS_PATH = "/products";
}
