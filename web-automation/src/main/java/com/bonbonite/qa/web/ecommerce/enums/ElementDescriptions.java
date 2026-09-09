package com.bonbonite.qa.web.ecommerce.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Nombres legibles de los elementos de la interfaz.
 *
 * <p>Cada acción y cada validación reciben una de estas descripciones, que es lo
 * que termina apareciendo en el reporte. Gracias a esto el paso se lee como
 * "Hacer clic en 'Botón iniciar sesión'" en lugar de mostrar un selector CSS, y
 * quien revise la evidencia no necesita conocer el código.</p>
 */
@Getter
@AllArgsConstructor
public enum ElementDescriptions {

  IS_VISIBLE("está visible"),
  IS_NOT_VISIBLE("no está visible"),
  IS_THE_EXPECTED("es el esperado"),

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
  REGISTER_SUBMIT_BUTTON("Botón registrarme");

  private final String value;
}
