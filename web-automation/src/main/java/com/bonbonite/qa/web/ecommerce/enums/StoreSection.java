package com.bonbonite.qa.web.ecommerce.enums;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Secciones del catálogo de la tienda.
 *
 * <p>Cada sección conoce el nombre con el que aparece en el menú y el identificador
 * con el que la Store API la reconoce. Tener ambos en el mismo lugar permite que un
 * escenario escrito en lenguaje de negocio —"la sección Zapatos"— se traduzca tanto
 * a un clic en el menú como a una consulta de precondición por servicio.</p>
 */
@Getter
@AllArgsConstructor
public enum StoreSection {

  SHOES("Zapatos", "zapatos-mujer"),
  BAGS("Bolsos", "bolsos-mujer"),
  BELTS("Cinturones", "cinturones-mujer"),
  ACCESSORIES("Accesorios", "accesorios-mujer"),
  OUTLET("Outlet", "outlet"),
  GIFT_CARDS("Bonos de regalo", "bonos-de-regalo");

  private final String menuName;
  private final String categorySlug;

  /**
   * Resuelve la sección a partir del nombre con el que aparece en el menú.
   *
   * @param menuName nombre visible de la sección, sin distinguir mayúsculas
   * @return sección correspondiente
   * @throws CustomException si el nombre no corresponde a ninguna sección
   */
  public static StoreSection byMenuName(String menuName) {
    return Arrays.stream(values())
      .filter(section -> section.getMenuName().equalsIgnoreCase(menuName.trim()))
      .findFirst()
      .orElseThrow(() -> new CustomException(
        "La sección '" + menuName + "' no está declarada en el catálogo"));
  }
}
