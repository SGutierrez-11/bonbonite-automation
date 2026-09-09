package com.bonbonite.qa.api.models.response.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Producto del catálogo, tal como lo expone la Store API de la tienda.
 *
 * <p>Solo se modelan los campos que la automatización necesita para elegir un
 * producto y validarlo en la interfaz. La anotación que ignora las propiedades
 * desconocidas es deliberada: el contrato del servicio devuelve muchos más campos
 * y puede crecer sin previo aviso, y eso no debe romper las pruebas.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {

  private long id;
  private String name;
  private String slug;
  private String type;
  private String permalink;
  private Prices prices;
  private List<Category> categories;
  private List<Attribute> attributes;
  private List<Variation> variations;

  @JsonProperty("is_in_stock")
  private boolean inStock;

  @JsonProperty("is_purchasable")
  private boolean purchasable;

  @JsonProperty("has_options")
  private boolean hasOptions;

  /** Precios del producto y formato de la moneda con la que se muestran. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Prices {

    /** Precio en la unidad mínima de la moneda, sin separadores. */
    private String price;

    @JsonProperty("regular_price")
    private String regularPrice;

    @JsonProperty("sale_price")
    private String salePrice;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("currency_symbol")
    private String currencySymbol;

    @JsonProperty("currency_thousand_separator")
    private String thousandSeparator;
  }

  /** Categoría a la que pertenece el producto. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Category {
    private long id;
    private String name;
    private String slug;
    private String link;
  }

  /** Atributo configurable del producto, por ejemplo color o talla. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Attribute {

    private long id;
    private String name;
    private String taxonomy;
    private List<Term> terms;

    @JsonProperty("has_variations")
    private boolean hasVariations;
  }

  /** Valor posible de un atributo. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Term {
    private long id;
    private String name;
    private String slug;
  }

  /** Combinación concreta de atributos que se puede agregar al carrito. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Variation {
    private long id;
    private List<VariationAttribute> attributes;
  }

  /** Atributo con el valor que toma en una variación. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class VariationAttribute {
    private String name;
    private String value;
  }
}
