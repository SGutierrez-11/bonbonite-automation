package com.bonbonite.qa.web.ecommerce.questions;

import static com.bonbonite.qa.web.actions.WaitActions.isTheElementInvisible;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.assertions.SoftAssertManager.getSoftAssert;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.ACCOUNT_DETAILS_FORM;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_THE_EXPECTED;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_VISIBLE;

import com.bonbonite.qa.web.ecommerce.pages.AccountDetailsPage;
import io.qameta.allure.Step;

/**
 * Validations on the account area, reachable only with an active session.
 */
public class AccountDetailsQuestions extends AccountDetailsPage {

  private static final int TIMEOUT = 20;

  /** Short wait used to confirm that something is absent, not to wait for it. */
  private static final int SHORT_TIMEOUT = 3;

  /**
   * Verifies that the customer reached the private area of the store, which is what
   * proves the sign in succeeded.
   */
  @Step("Verificar que la sesión quedó iniciada")
  public void verifySessionIsOpen() {
    getSoftAssert().assertTrue(isTheElementInvisible(getTxtLoginDocumentNumber(), TIMEOUT),
      "El formulario de acceso ya no debe mostrarse una vez iniciada la sesión");
    getSoftAssert().assertFalse(isTheElementVisible(getLblErrorMessage(), SHORT_TIMEOUT),
      "La tienda no debe mostrar un error de acceso tras un inicio de sesión válido");
  }

  /**
   * Verifies that the account details form is available for editing.
   */
  @Step("Verificar el formulario de detalles de la cuenta")
  public void verifyAccountDetailsFormIsDisplayed() {
    getSoftAssert().assertTrue(isTheElementVisible(getFrmAccountDetails(), TIMEOUT),
      ACCOUNT_DETAILS_FORM.getValue() + " " + IS_VISIBLE.getValue());
  }

  /**
   * Verifies that the new value survived a reload of the screen.
   *
   * <p>The check is the persisted value rather than a confirmation banner: the theme
   * saves the profile without showing one, so the value itself is the only honest
   * evidence that the change was stored.</p>
   *
   * @param expectedFirstName value the field is expected to hold after saving
   * @param actualFirstName   value the field holds after reloading the screen
   */
  @Step("Verificar que el cambio de datos quedó guardado")
  public void verifyChangeWasSaved(String expectedFirstName, String actualFirstName) {
    getSoftAssert().assertEquals(actualFirstName, expectedFirstName,
      "El nombre guardado en la cuenta " + IS_THE_EXPECTED.getValue());
  }

  /**
   * Verifies that the value was restored to what it was before the scenario ran.
   *
   * @param originalFirstName value the account had at the start of the scenario
   * @param actualFirstName   value the account holds at the end
   */
  @Step("Verificar que los datos quedaron como estaban")
  public void verifyChangeWasReverted(String originalFirstName, String actualFirstName) {
    getSoftAssert().assertEquals(actualFirstName, originalFirstName,
      "El nombre de la cuenta debe quedar restaurado a su valor original");
  }
}
