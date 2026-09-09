package com.bonbonite.qa.api.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Store customer data.
 *
 * <p>The store identifies a customer by their national id number instead of their
 * email, so {@code documentNumber} is the credential used to sign in.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRequest {

  private String documentNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String phone;
  private String address;
  private String postcode;
}
