package com.bonbonite.qa.web.ecommerce.enums;

/**
 * Llaves con las que los pasos almacenan y recuperan datos del contexto del escenario.
 *
 * <p>Usar constantes en lugar de cadenas sueltas hace que el compilador detecte los
 * errores de escritura y permite ver de un vistazo qué información viaja entre pasos.</p>
 */
public enum Keys {

  SELECTED_SECTION,
  SELECTED_PRODUCT,
  REGISTERED_CUSTOMER,
  CART_SUBTOTAL,
  ORDER_SUMMARY,
  ORIGINAL_ACCOUNT_DATA
}
