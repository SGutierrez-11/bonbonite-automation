package com.bonbonite.qa.api.models.response.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Catalog product as exposed by the Store API.
 *
 * <p>Only the fields the automation needs to pick a product and validate it on the
 * interface are modelled. Ignoring unknown properties is deliberate: the service
 * contract returns many more fields and may grow without notice, and that must not
 * break the tests.</p>
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

  /** Product prices and the formatting of the currency they are displayed with. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Prices {

    /** Price in the currency minor unit, with no separators. */
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

  /** Category the product belongs to. */
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

  /** Configurable product attribute, such as colour or size. */
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

  /** Possible value of an attribute. */
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

  /** Specific combination of attributes that can be added to the cart. */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Variation {
    private long id;
    private List<VariationAttribute> attributes;
  }

  /** Attribute together with the value it takes on a variation. */
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
