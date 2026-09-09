package com.bonbonite.qa.web.ecommerce.questions;

import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.assertions.SoftAssertManager.getSoftAssert;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_ENABLED;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.IS_VISIBLE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_CONSENT_CHECKBOX;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_DESCRIPTION;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_EMAIL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_FORM;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_FULL_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_PHONE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_SUBMIT_BUTTON;

import com.bonbonite.qa.web.ecommerce.pages.PqrsPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * Validations on the customer service request screen.
 */
public class PqrsQuestions extends PqrsPage {

  private static final int TIMEOUT = 15;

  /**
   * Verifies that the request form and its mandatory fields are available.
   */
  @Step("Verificar el formulario de PQRS")
  public void verifyFormIsDisplayed() {
    assertVisible(getFrmPqrs(), PQRS_FORM.getValue());
    assertVisible(getTxtFullName(), PQRS_FULL_NAME.getValue());
    assertVisible(getTxtPhone(), PQRS_PHONE.getValue());
    assertVisible(getTxtEmail(), PQRS_EMAIL.getValue());
    assertVisible(getTxtDescription(), PQRS_DESCRIPTION.getValue());
    assertVisible(getBtnSubmit(), PQRS_SUBMIT_BUTTON.getValue());
  }

  /**
   * Verifies that the request is complete and ready to be sent, which is the state
   * the automation stops at.
   *
   * @param expectedEmail email that was typed into the form
   */
  @Step("Verificar que la solicitud quedó lista para enviarse")
  public void verifyRequestIsReadyToSubmit(String expectedEmail) {
    getSoftAssert().assertEquals(getTxtEmail().getDomProperty("value"), expectedEmail,
      "El correo de la solicitud es el esperado");
    getSoftAssert().assertFalse(getTxtDescription().getDomProperty("value").isBlank(),
      PQRS_DESCRIPTION.getValue() + " no puede quedar vacía");
    getSoftAssert().assertTrue(getChkConsent().isSelected(),
      PQRS_CONSENT_CHECKBOX.getValue() + " debe quedar marcada");
    getSoftAssert().assertTrue(getBtnSubmit().isEnabled(),
      PQRS_SUBMIT_BUTTON.getValue() + " " + IS_ENABLED.getValue());
  }

  /**
   * Verifies that submitting the empty form does not create a request and that the
   * site explains what is missing.
   */
  @Step("Verificar las validaciones de los campos obligatorios")
  public void verifyMandatoryFieldsAreValidated() {
    getSoftAssert().assertFalse(getLstValidationMessages().isEmpty(),
      "El sitio debe mostrar al menos un mensaje de validación al enviar la solicitud vacía");
    getSoftAssert().assertTrue(isTheElementVisible(getFrmPqrs(), TIMEOUT),
      "El formulario debe permanecer visible tras un envío inválido");
  }

  private void assertVisible(WebElement element, String description) {
    getSoftAssert().assertTrue(isTheElementVisible(element, TIMEOUT),
      description + " " + IS_VISIBLE.getValue());
  }
}
