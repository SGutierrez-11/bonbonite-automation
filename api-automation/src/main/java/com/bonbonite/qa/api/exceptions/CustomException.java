package com.bonbonite.qa.api.exceptions;

/**
 * Excepción propia del framework.
 *
 * <p>Se usa para diferenciar los fallos de infraestructura de la automatización
 * (configuración ausente, dato inexistente, respuesta inesperada de un servicio)
 * de los fallos funcionales del producto bajo prueba, que se reportan mediante
 * aserciones.</p>
 */
public class CustomException extends RuntimeException {

  /**
   * Crea la excepción con un mensaje descriptivo.
   *
   * @param message detalle de la causa del fallo
   */
  public CustomException(String message) {
    super(message);
  }

  /**
   * Crea la excepción conservando la causa original.
   *
   * @param message detalle de la causa del fallo
   * @param cause   excepción que originó el fallo
   */
  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }
}
