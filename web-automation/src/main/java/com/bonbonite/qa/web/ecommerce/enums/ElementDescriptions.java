package com.bonbonite.qa.web.ecommerce.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Human readable element names rendered in the execution report.
 *
 * <p>Every action and every validation receives one of these descriptions, which is
 * what ends up in the report. Thanks to that, a step reads as "Hacer clic en 'Botón
 * iniciar sesión'" instead of showing a CSS selector, and whoever reviews the
 * evidence does not need to know the code.</p>
 *
 * <p>The values are written in Spanish on purpose: they are business facing text,
 * unlike the code around them.</p>
 */
@Getter
@AllArgsConstructor
public enum ElementDescriptions {

  IS_VISIBLE("está visible"),
  IS_NOT_VISIBLE("no está visible"),
  IS_THE_EXPECTED("es el esperado"),
  IS_ENABLED("está habilitado"),

  COOKIE_BANNER("Banner de cookies"),
  COOKIE_REJECT_BUTTON("Botón rechazar todas las cookies"),
  COOKIE_ACCEPT_BUTTON("Botón aceptar todas las cookies"),

  HEADER_ACCOUNT_LINK("Enlace a mi cuenta"),
  HEADER_CART_LINK("Enlace al carrito"),
  HEADER_CART_COUNT("Contador de productos del carrito"),
  HEADER_MAIN_MENU("Menú principal de categorías"),
  HEADER_SHOES_MENU("Menú Zapatos"),
  HEADER_BAGS_MENU("Menú Bolsos"),
  HEADER_BELTS_MENU("Menú Cinturones"),
  HEADER_ACCESSORIES_MENU("Menú Accesorios"),
  HEADER_OUTLET_MENU("Menú Outlet"),

  LOGIN_DOCUMENT_INPUT("Campo número de cédula"),
  LOGIN_PASSWORD_INPUT("Campo contraseña"),
  LOGIN_REMEMBER_CHECKBOX("Casilla recuérdame"),
  LOGIN_SUBMIT_BUTTON("Botón iniciar sesión"),
  LOGIN_ERROR_MESSAGE("Mensaje de error del inicio de sesión"),

  REGISTER_LINK("Enlace regístrate"),
  REGISTER_FORM("Formulario de registro"),
  REGISTER_DOCUMENT_INPUT("Campo número de cédula del registro"),
  REGISTER_FIRST_NAME_INPUT("Campo nombre"),
  REGISTER_LAST_NAME_INPUT("Campo apellido"),
  REGISTER_EMAIL_INPUT("Campo correo electrónico"),
  REGISTER_PASSWORD_INPUT("Campo contraseña del registro"),
  REGISTER_CONFIRM_PASSWORD_INPUT("Campo confirmación de contraseña"),
  REGISTER_NEWSLETTER_CHECKBOX("Casilla de autorización de novedades"),
  REGISTER_PRIVACY_CHECKBOX("Casilla de política de tratamiento de datos"),
  REGISTER_SUBMIT_BUTTON("Botón registrarme"),

  ACCOUNT_MENU("Menú de mi cuenta"),
  ACCOUNT_LOGOUT_LINK("Enlace cerrar sesión"),
  ACCOUNT_DETAILS_FORM("Formulario de detalles de la cuenta"),
  ACCOUNT_FIRST_NAME_INPUT("Campo nombre de la cuenta"),
  ACCOUNT_LAST_NAME_INPUT("Campo apellido de la cuenta"),
  ACCOUNT_DISPLAY_NAME_INPUT("Campo nombre para mostrar"),
  ACCOUNT_SAVE_BUTTON("Botón guardar cambios"),
  ACCOUNT_SUCCESS_MESSAGE("Mensaje de confirmación de la cuenta"),

  PRODUCT_NAME("Nombre del producto"),
  PRODUCT_PRICE("Precio del producto"),
  PRODUCT_SIZE_BUTTON("Botón de talla"),
  PRODUCT_SIZE_SELECT("Selector de talla"),
  PRODUCT_QUANTITY_INPUT("Campo cantidad"),
  PRODUCT_ADD_TO_CART_BUTTON("Botón añadir al carrito"),

  CART_ITEM("Producto del carrito"),
  CART_ITEM_NAME("Nombre del producto en el carrito"),
  CART_SUBTOTAL_LABEL("Subtotal del carrito"),
  CART_TOTAL_LABEL("Total del carrito"),
  CART_CHECKOUT_BUTTON("Botón finalizar compra"),

  CHECKOUT_FORM("Formulario de finalizar compra"),
  CHECKOUT_DOCUMENT_TYPE("Tipo de documento"),
  CHECKOUT_DOCUMENT_NUMBER("Número de documento de facturación"),
  CHECKOUT_FIRST_NAME("Nombre de facturación"),
  CHECKOUT_LAST_NAME("Apellido de facturación"),
  CHECKOUT_EMAIL("Correo de facturación"),
  CHECKOUT_PHONE("Teléfono de facturación"),
  CHECKOUT_COUNTRY("País"),
  CHECKOUT_STATE("Departamento"),
  CHECKOUT_CITY("Ciudad"),
  CHECKOUT_ADDRESS("Dirección de envío"),
  CHECKOUT_POSTCODE("Código postal"),
  CHECKOUT_ORDER_REVIEW("Resumen de la orden"),
  CHECKOUT_ORDER_PRODUCT("Producto del resumen de la orden"),
  CHECKOUT_SHIPPING_METHODS("Métodos de envío"),
  CHECKOUT_PAYMENT_METHOD("Método de pago"),
  CHECKOUT_PLACE_ORDER_BUTTON("Botón registrar orden"),

  PQRS_FORM("Formulario de PQRS"),
  PQRS_SALES_POINT("Punto de venta"),
  PQRS_FULL_NAME("Nombre completo del cliente"),
  PQRS_ADDRESS("Dirección y ciudad"),
  PQRS_DOCUMENT_TYPE("Tipo de documento de la PQRS"),
  PQRS_DOCUMENT_NUMBER("Número de documento de la PQRS"),
  PQRS_PHONE("Teléfono de la PQRS"),
  PQRS_EMAIL("Correo electrónico de la PQRS"),
  PQRS_REQUEST_TYPE("Tipo de solicitud"),
  PQRS_REASON("Causal de la solicitud"),
  PQRS_DESCRIPTION("Descripción de la solicitud"),
  PQRS_CONSENT_CHECKBOX("Casilla de autorización de datos"),
  PQRS_SUBMIT_BUTTON("Botón crear PQRS");

  private final String value;
}
