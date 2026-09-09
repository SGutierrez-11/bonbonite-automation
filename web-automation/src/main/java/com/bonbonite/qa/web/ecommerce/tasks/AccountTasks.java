package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.check;
import static com.bonbonite.qa.web.actions.CommonActions.click;
import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.CommonActions.sendKeys;
import static com.bonbonite.qa.web.actions.CommonActions.sendSecretKeys;
import static com.bonbonite.qa.web.driver.BrowserProperties.ACCOUNT_URL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_DOCUMENT_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_PASSWORD_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.LOGIN_SUBMIT_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_CONFIRM_PASSWORD_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_DOCUMENT_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_EMAIL_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_FIRST_NAME_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_LAST_NAME_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_LINK;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_PASSWORD_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.REGISTER_PRIVACY_CHECKBOX;

import com.bonbonite.qa.api.models.request.CustomerRequest;
import com.bonbonite.qa.web.ecommerce.pages.AccountPage;
import io.qameta.allure.Step;

/**
 * Actions on the account screen: signing in and filling the registration form.
 */
public class AccountTasks extends AccountPage {

  /**
   * Opens the account screen directly by its address.
   */
  @Step("Abrir la pantalla de mi cuenta")
  public void openAccountPage() {
    openPage(ACCOUNT_URL);
  }

  /**
   * Signs a customer in with their document number and password.
   *
   * @param customer customer holding the credentials to use
   */
  @Step("Iniciar sesión en la tienda")
  public void performLogin(CustomerRequest customer) {
    sendKeys(getTxtLoginDocumentNumber(), customer.getDocumentNumber(),
      LOGIN_DOCUMENT_INPUT.getValue());
    sendSecretKeys(getTxtLoginPassword(), customer.getPassword(), LOGIN_PASSWORD_INPUT.getValue());
    click(getBtnSignIn(), LOGIN_SUBMIT_BUTTON.getValue());
  }

  /**
   * Submits the sign in form without filling any field, to exercise the required
   * field validation.
   */
  @Step("Enviar el formulario de inicio de sesión vacío")
  public void submitEmptyLoginForm() {
    click(getBtnSignIn(), LOGIN_SUBMIT_BUTTON.getValue());
  }

  /**
   * Switches the account screen to the registration form.
   *
   * <p>The link sits below the fold on the default window size, so the screen is
   * scrolled first: an element outside the viewport is reported as displayed but the
   * click against it is unreliable.</p>
   */
  @Step("Abrir el formulario de registro")
  public void openRegistrationForm() {
    scrollToElement(getLnkShowRegister(), REGISTER_LINK.getValue());
    clickHandlingInterception(getLnkShowRegister(), REGISTER_LINK.getValue());
  }

  /**
   * Fills the registration form with the data of the given customer.
   *
   * <p>The form is filled but never submitted. Submitting it would create a real
   * account in the client's production database, which is outside the agreed scope.
   * Stopping here still exercises every field and every client side validation.</p>
   *
   * @param customer customer whose data is typed into the form
   */
  @Step("Diligenciar el formulario de registro")
  public void fillRegistrationForm(CustomerRequest customer) {
    sendKeys(getTxtRegisterDocumentNumber(), customer.getDocumentNumber(),
      REGISTER_DOCUMENT_INPUT.getValue());
    sendKeys(getTxtFirstName(), customer.getFirstName(), REGISTER_FIRST_NAME_INPUT.getValue());
    sendKeys(getTxtLastName(), customer.getLastName(), REGISTER_LAST_NAME_INPUT.getValue());
    sendKeys(getTxtEmail(), customer.getEmail(), REGISTER_EMAIL_INPUT.getValue());
    sendSecretKeys(getTxtRegisterPassword(), customer.getPassword(),
      REGISTER_PASSWORD_INPUT.getValue());
    sendSecretKeys(getTxtConfirmPassword(), customer.getPassword(),
      REGISTER_CONFIRM_PASSWORD_INPUT.getValue());
    check(getChkPrivacyPolicy(), REGISTER_PRIVACY_CHECKBOX.getValue());
  }
}
