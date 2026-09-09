package com.bonbonite.qa.web.ecommerce.enums;

/**
 * Keys the steps use to store and retrieve data from the scenario context.
 *
 * <p>Using constants instead of loose strings lets the compiler catch typos and
 * makes it possible to see at a glance which information travels between steps.</p>
 */
public enum Keys {

  SELECTED_SECTION,
  SELECTED_PRODUCT,
  SELECTED_SIZE,
  PRODUCT_PRICE,
  CART_SUBTOTAL,
  REGISTERED_CUSTOMER,
  NEW_CUSTOMER,
  ORIGINAL_FIRST_NAME
}
