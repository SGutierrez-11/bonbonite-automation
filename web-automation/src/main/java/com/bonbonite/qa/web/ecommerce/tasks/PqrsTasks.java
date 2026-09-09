package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.check;
import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.CommonActions.sendKeys;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_ADDRESS;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_CONSENT_CHECKBOX;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_DESCRIPTION;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_DOCUMENT_NUMBER;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_EMAIL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_FULL_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_PHONE;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.PQRS_SUBMIT_BUTTON;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.api.models.request.CustomerRequest;
import com.bonbonite.qa.web.ecommerce.pages.PqrsPage;
import io.qameta.allure.Step;

/**
 * Actions on the customer service request screen.
 *
 * <p>The form is filled and its validations are exercised, but it is never sent.
 * Submitting it would open a real request in the client's customer service queue,
 * which is outside the agreed scope.</p>
 */
public class PqrsTasks extends PqrsPage {

  private static final int FORM_RENDER_TIMEOUT = 60;

  /**
   * Waits until the request form finished rendering.
   *
   * <p>The form is built by a plugin after the page load, and it is the heaviest
   * screen of the site. Under parallel execution it can take noticeably longer than
   * the default wait, so the task blocks on the submit button — the last element the
   * plugin renders — before touching any field.</p>
   *
   * @throws CustomException if the form never finishes rendering
   */
  @Step("Esperar a que el formulario de PQRS termine de cargar")
  public void awaitFormIsRendered() {
    if (!isTheElementVisible(getBtnSubmit(), FORM_RENDER_TIMEOUT)) {
      throw new CustomException(String.format(
        "El formulario de PQRS no terminó de cargar después de %d segundos",
        FORM_RENDER_TIMEOUT));
    }
  }

  /**
   * Fills the request with the data of the given customer and a description.
   *
   * @param customer    customer whose contact data is typed into the form
   * @param description text of the request
   */
  @Step("Diligenciar la solicitud de PQRS")
  public void fillRequest(CustomerRequest customer, String description) {
    awaitFormIsRendered();
    scrollToElement(getTxtFullName(), PQRS_FULL_NAME.getValue());
    sendKeys(getTxtFullName(), customer.getFirstName() + " " + customer.getLastName(),
      PQRS_FULL_NAME.getValue());
    sendKeys(getTxtAddress(), customer.getAddress(), PQRS_ADDRESS.getValue());
    sendKeys(getTxtDocumentNumber(), customer.getDocumentNumber(),
      PQRS_DOCUMENT_NUMBER.getValue());
    sendKeys(getTxtPhone(), customer.getPhone(), PQRS_PHONE.getValue());
    sendKeys(getTxtEmail(), customer.getEmail(), PQRS_EMAIL.getValue());
    sendKeys(getTxtDescription(), description, PQRS_DESCRIPTION.getValue());
    check(getChkConsent(), PQRS_CONSENT_CHECKBOX.getValue());
  }

  /**
   * Submits the request without filling any field, to exercise the required field
   * validation of the form.
   */
  @Step("Enviar la solicitud de PQRS vacía")
  public void submitEmptyRequest() {
    awaitFormIsRendered();
    scrollToElement(getBtnSubmit(), PQRS_SUBMIT_BUTTON.getValue());
    clickHandlingInterception(getBtnSubmit(), PQRS_SUBMIT_BUTTON.getValue());
  }
}
