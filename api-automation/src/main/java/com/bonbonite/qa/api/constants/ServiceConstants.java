package com.bonbonite.qa.api.constants;

import static com.bonbonite.qa.api.config.PropertiesManager.getParameter;

import lombok.experimental.UtilityClass;

/**
 * Direcciones base de los servicios que consume la automatización.
 *
 * <p>Los valores se resuelven una sola vez al cargar la clase, con la precedencia
 * definida en el gestor de propiedades, de modo que apuntar a otro ambiente sea
 * solo cambiar un parámetro de ejecución.</p>
 */
@UtilityClass
public class ServiceConstants {

  private static final String API_MODULE = "api";

  /** Esquema y dominio de la tienda. */
  public static final String STORE_BASE_URI = getParameter("store.base.uri", API_MODULE);

  /** Ruta común de los recursos públicos de la Store API. */
  public static final String STORE_BASE_PATH = getParameter("store.base.path", API_MODULE);

  /** Recurso de productos del catálogo. */
  public static final String PRODUCTS_PATH = "/products";
}
