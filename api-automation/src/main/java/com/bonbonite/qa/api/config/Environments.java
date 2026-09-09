package com.bonbonite.qa.api.config;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Environments the automation can be executed against.
 *
 * <p>The Bon-bonite site only exposes production, so there is a single value today.
 * The enum is kept as an extension point: when a testing environment appears, adding
 * the constant and its properties file is enough, with no change to the rest of the
 * framework.</p>
 */
@Getter
@AllArgsConstructor
public enum Environments {

  PROD("prod");

  private final String name;

  /**
   * Resolves an environment from its name.
   *
   * @param name environment name, case insensitive; may be null
   * @return the matching environment, or {@link #PROD} when the name is null or blank
   * @throws CustomException if the name does not match any declared environment
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
