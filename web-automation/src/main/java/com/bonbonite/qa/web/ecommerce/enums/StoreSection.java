package com.bonbonite.qa.web.ecommerce.enums;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Sections of the store catalog.
 *
 * <p>Each section knows the name it shows in the menu and the identifier the Store
 * API recognises it by. Keeping both together lets a scenario written in business
 * language — "the Zapatos section" — translate into either a click on the menu or a
 * precondition query through the service.</p>
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
   * Resolves a section from the name it shows in the menu.
   *
   * @param menuName visible name of the section, case insensitive
   * @return the matching section
   * @throws CustomException if the name does not match any section
   */
  public static StoreSection byMenuName(String menuName) {
    return Arrays.stream(values())
      .filter(section -> section.getMenuName().equalsIgnoreCase(menuName.trim()))
      .findFirst()
      .orElseThrow(() -> new CustomException(
        "La sección '" + menuName + "' no está declarada en el catálogo"));
  }
}
