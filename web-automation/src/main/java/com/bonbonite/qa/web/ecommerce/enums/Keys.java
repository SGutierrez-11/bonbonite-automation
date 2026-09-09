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
  REGISTERED_CUSTOMER,
  CART_SUBTOTAL,
  ORDER_SUMMARY,
  ORIGINAL_ACCOUNT_DATA
}
