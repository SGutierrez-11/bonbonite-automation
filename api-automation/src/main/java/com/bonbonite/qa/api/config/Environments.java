package com.bonbonite.qa.api.config;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Entornos contra los que puede ejecutarse la automatización.
 *
 * <p>El sitio de Bon-bonite solo expone producción, de modo que hoy existe un
 * único valor. La enum se mantiene como punto de extensión: al aparecer un
 * ambiente de pruebas basta agregar la constante y su archivo de propiedades,
 * sin tocar el resto del framework.</p>
 */
@Getter
@AllArgsConstructor
public enum Environments {

  PROD("prod");

  private final String name;

  /**
   * Resuelve el entorno a partir de su nombre.
   *
   * @param name nombre del entorno, sin distinguir mayúsculas; puede ser nulo
   * @return el entorno correspondiente, o {@link #PROD} si el nombre es nulo o vacío
   * @throws CustomException si el nombre no corresponde a ningún entorno declarado
   */
  public static Environments byName(String name) {
    if (name == null || name.isBlank()) {
      return PROD;
    }
    return Arrays.stream(values())
      .filter(environment -> environment.getName().equalsIgnoreCase(name.trim()))
      .findFirst()
      .orElseThrow(() -> new CustomException(
        "El entorno '" + name + "' no está declarado. Opciones válidas: "
          + Arrays.toString(values())));
  }
}
