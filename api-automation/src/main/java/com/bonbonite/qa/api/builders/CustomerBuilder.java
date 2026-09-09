package com.bonbonite.qa.api.builders;

import static com.bonbonite.qa.api.config.PropertiesManager.getParameter;

import com.bonbonite.qa.api.models.request.CustomerRequest;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

/**
 * Factory of customers used by the scenarios.
 *
 * <p>It offers two sources. The registered customer comes from configuration and is
 * the single real account the project is allowed to use, so its credentials never
 * live in the repository. The random customer is generated on the fly and is used
 * only to exercise the registration form, never to create an account.</p>
 */
@UtilityClass
public class CustomerBuilder {

  private static final String WEB_MODULE = "web";
  private static final Faker FAKER = new Faker(new Locale("es", "CO"));

  /**
   * Returns the real customer the authenticated scenarios run with.
   *
   * <p>The document number and the password are read from the environment, never
   * from a versioned file.</p>
   *
   * @return the registered customer with its credentials
   */
  public static CustomerRequest registeredCustomer() {
    return CustomerRequest.builder()
      .documentNumber(getParameter("customer.document", WEB_MODULE))
      .password(getParameter("customer.password", WEB_MODULE))
      .build();
  }

  /**
   * Returns a customer with credentials that do not belong to any account.
   *
   * @return a customer whose sign in attempt must be rejected
   */
  public static CustomerRequest unknownCustomer() {
    return CustomerRequest.builder()
      .documentNumber(randomDocumentNumber())
      .password("NoExiste" + FAKER.number().numberBetween(100, 999) + "!")
      .build();
  }

  /**
   * Builds a customer with random data ready to fill the registration form.
   *
   * @return a customer with a generated document number, name, email and password
   */
  public static CustomerRequest randomCustomer() {
    String firstName = FAKER.name().firstName();
    String lastName = FAKER.name().lastName();
    return CustomerRequest.builder()
      .documentNumber(randomDocumentNumber())
      .firstName(firstName)
      .lastName(lastName)
      .email(randomEmail(firstName, lastName))
      .password(strongPassword())
      .phone("3" + ThreadLocalRandom.current().nextLong(100000000L, 999999999L))
      .address(FAKER.address().streetAddress())
      .postcode(String.valueOf(ThreadLocalRandom.current().nextInt(50001, 59999)))
      .build();
  }

  /**
   * Generates a number with the length of a Colombian national id.
   *
   * @return a ten digit document number
   */
  public static String randomDocumentNumber() {
    return String.valueOf(ThreadLocalRandom.current().nextLong(1000000000L, 1999999999L));
  }

  private static String randomEmail(String firstName, String lastName) {
    return String.format("%s.%s.%d@correo-de-prueba.com",
      normalize(firstName), normalize(lastName), System.currentTimeMillis() % 1000000);
  }

  private static String normalize(String value) {
    return java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
      .replaceAll("[^\\p{ASCII}]", "")
      .toLowerCase(Locale.ROOT)
      .replaceAll("[^a-z]", "");
  }

  private static String strongPassword() {
    return "Prueba" + ThreadLocalRandom.current().nextInt(1000, 9999) + "!Qa";
  }
}
