package com.bonbonite.qa.web.ecommerce.questions;

import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.assertions.SoftAssertManager.getSoftAssert;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_THE_EXPECTED;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_VISIBLE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_DOCUMENT_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_ERROR_MESSAGE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_PASSWORD_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_SUBMIT_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_CONFIRM_PASSWORD_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_DOCUMENT_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_EMAIL_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_FIRST_NAME_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_LAST_NAME_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_PASSWORD_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_PRIVACY_CHECKBOX;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_SUBMIT_BUTTON;

import com.bonbonite.qa.web.ecommerce.pages.AccountPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * Validations on the account screen.
 */
public class AccountQuestions extends AccountPage {

  private static final int TIMEOUT = 15;

  /**
   * Verifies that the fields required to sign in are displayed.
   */
  @Step("Verificar el formulario de inicio de sesión")
  public void verifyLoginFormIsDisplayed() {
    assertVisible(getTxtLoginDocumentNumber(), LOGIN_DOCUMENT_INPUT.getValue());
    assertVisible(getTxtLoginPassword(), LOGIN_PASSWORD_INPUT.getValue());
    assertVisible(getBtnSignIn(), LOGIN_SUBMIT_BUTTON.getValue());
  }

  /**
   * Verifies that the store rejected the sign in attempt and explained why.
   */
  @Step("Verificar el mensaje de error del inicio de sesión")
  public void verifyLoginWasRejected() {
    boolean visible = isTheElementVisible(getLblErrorMessage(), TIMEOUT);
    getSoftAssert().assertTrue(visible,
      LOGIN_ERROR_MESSAGE.getValue() + " " + IS_VISIBLE.getValue());
    if (visible) {
      getSoftAssert().assertFalse(
        getText(getLblErrorMessage(), LOGIN_ERROR_MESSAGE.getValue()).isBlank(),
        "El mensaje de error del inicio de sesión no debe estar vacío");
    }
  }

  /**
   * Verifies that every field of the registration form is available to the customer.
   */
  @Step("Verificar el formulario de registro")
  public void verifyRegistrationFormIsDisplayed() {
    assertVisible(getTxtRegisterDocumentNumber(), REGISTER_DOCUMENT_INPUT.getValue());
    assertVisible(getTxtFirstName(), REGISTER_FIRST_NAME_INPUT.getValue());
    assertVisible(getTxtLastName(), REGISTER_LAST_NAME_INPUT.getValue());
    assertVisible(getTxtEmail(), REGISTER_EMAIL_INPUT.getValue());
    assertVisible(getTxtRegisterPassword(), REGISTER_PASSWORD_INPUT.getValue());
    assertVisible(getTxtConfirmPassword(), REGISTER_CONFIRM_PASSWORD_INPUT.getValue());
    assertVisible(getBtnRegister(), REGISTER_SUBMIT_BUTTON.getValue());
  }

  /**
   * Verifies that the registration form carries the data that was typed and that the
   * data processing authorization was accepted, which is the state right before the
   * form would be submitted.
   *
   * @param documentNumber document number that was typed
   * @param email          email that was typed
   */
  @Step("Verificar que el formulario de registro quedó listo para enviarse")
  public void verifyRegistrationFormIsReadyToSubmit(String documentNumber, String email) {
    getSoftAssert().assertEquals(
      getTxtRegisterDocumentNumber().getDomProperty("value"), documentNumber,
      "El número de cédula del registro " + IS_THE_EXPECTED.getValue());
    getSoftAssert().assertEquals(getTxtEmail().getDomProperty("value"), email,
      "El correo del registro " + IS_THE_EXPECTED.getValue());
    getSoftAssert().assertTrue(getChkPrivacyPolicy().isSelected(),
      REGISTER_PRIVACY_CHECKBOX.getValue() + " debe quedar marcada");
    getSoftAssert().assertTrue(getBtnRegister().isEnabled(),
      REGISTER_SUBMIT_BUTTON.getValue() + " debe estar habilitado");
  }

  private void assertVisible(WebElement element, String description) {
    getSoftAssert().assertTrue(isTheElementVisible(element, TIMEOUT),
      description + " " + IS_VISIBLE.getValue());
  }

}
