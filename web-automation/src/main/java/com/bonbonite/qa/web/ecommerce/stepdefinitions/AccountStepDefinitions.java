package com.bonbonite.qa.web.ecommerce.stepdefinitions;

import static com.bonbonite.qa.api.context.TestContextManager.getTestContext;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.NEW_CUSTOMER;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.ORIGINAL_FIRST_NAME;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.REGISTERED_CUSTOMER;

import com.bonbonite.qa.api.builders.CustomerBuilder;
import com.bonbonite.qa.api.models.request.CustomerRequest;
import com.bonbonite.qa.web.ecommerce.questions.AccountDetailsQuestions;
import com.bonbonite.qa.web.ecommerce.questions.AccountQuestions;
import com.bonbonite.qa.web.ecommerce.tasks.AccountDetailsTasks;
import com.bonbonite.qa.web.ecommerce.tasks.AccountTasks;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

/**
 * Steps for signing in, registering and maintaining the customer account.
 */
public class AccountStepDefinitions {

  private final AccountTasks accountTasks = new AccountTasks();
  private final AccountQuestions accountQuestions = new AccountQuestions();
  private final AccountDetailsTasks accountDetailsTasks = new AccountDetailsTasks();
  private final AccountDetailsQuestions accountDetailsQuestions = new AccountDetailsQuestions();

  /**
   * Opens the account screen and checks that the sign in form is offered.
   */
  @Dado("que estoy en la pantalla de mi cuenta")
  public void iAmOnTheAccountScreen() {
    accountTasks.openAccountPage();
    accountQuestions.verifyLoginFormIsDisplayed();
  }

  /**
   * Signs in with the real customer configured for the project.
   */
  @Cuando("inicio sesión con el cliente registrado")
  public void signInWithTheRegisteredCustomer() {
    CustomerRequest customer = CustomerBuilder.registeredCustomer();
    getTestContext().set(REGISTERED_CUSTOMER.name(), customer);
    accountTasks.performLogin(customer);
  }

  /**
   * Signs in with credentials that belong to no account.
   */
  @Cuando("intento iniciar sesión con credenciales inexistentes")
  public void signInWithUnknownCredentials() {
    accountTasks.performLogin(CustomerBuilder.unknownCustomer());
  }

  /**
   * Signs in with a valid document number and a wrong password.
   */
  @Cuando("intento iniciar sesión con la contraseña incorrecta")
  public void signInWithAWrongPassword() {
    CustomerRequest customer = CustomerBuilder.registeredCustomer();
    accountTasks.performLogin(CustomerRequest.builder()
      .documentNumber(customer.getDocumentNumber())
      .password("ContrasenaIncorrecta123!")
      .build());
  }

  /**
   * Submits the sign in form with no data at all.
   */
  @Cuando("envío el formulario de inicio de sesión vacío")
  public void submitTheEmptyLoginForm() {
    accountTasks.submitEmptyLoginForm();
  }

  /**
   * Verifies that the customer reached the private area of the store.
   */
  @Entonces("la tienda me reconoce como cliente autenticado")
  public void theStoreRecognisesMeAsSignedIn() {
    accountDetailsQuestions.verifySessionIsOpen();
  }

  /**
   * Verifies that the store rejected the attempt and explained why.
   */
  @Entonces("la tienda rechaza el acceso e informa el error")
  public void theStoreRejectsTheAccess() {
    accountQuestions.verifyLoginWasRejected();
  }

  /**
   * Opens the registration form from the account screen.
   */
  @Cuando("abro el formulario de registro")
  public void openTheRegistrationForm() {
    accountTasks.openRegistrationForm();
    accountQuestions.verifyRegistrationFormIsDisplayed();
  }

  /**
   * Fills the registration form with generated data, without submitting it.
   */
  @Y("diligencio el registro con datos válidos")
  public void fillTheRegistrationFormWithValidData() {
    CustomerRequest customer = CustomerBuilder.randomCustomer();
    getTestContext().set(NEW_CUSTOMER.name(), customer);
    accountTasks.fillRegistrationForm(customer);
  }

  /**
   * Verifies that the registration form is complete and ready to be submitted, which
   * is the agreed limit for this scenario.
   */
  @Entonces("el formulario de registro queda listo para enviarse")
  public void theRegistrationFormIsReadyToSubmit() {
    CustomerRequest customer = getTestContext().get(NEW_CUSTOMER.name());
    accountQuestions.verifyRegistrationFormIsReadyToSubmit(
      customer.getDocumentNumber(), customer.getEmail());
  }

  /**
   * Opens the account details screen and remembers the value it starts with, so the
   * scenario can restore it afterwards.
   */
  @Cuando("abro los detalles de mi cuenta")
  public void openTheAccountDetails() {
    accountDetailsTasks.openAccountDetails();
    accountDetailsQuestions.verifyAccountDetailsFormIsDisplayed();
    getTestContext().set(ORIGINAL_FIRST_NAME.name(), accountDetailsTasks.getCurrentFirstName());
  }

  /**
   * Changes the first name of the account and saves it.
   *
   * @param firstName new value for the first name
   */
  @Y("cambio mi nombre a {string}")
  public void changeMyFirstNameTo(String firstName) {
    accountDetailsTasks.updateFirstName(firstName);
  }

  /**
   * Verifies that the change was confirmed and persisted after reloading the screen.
   *
   * @param expectedFirstName value the account is expected to hold
   */
  @Entonces("la tienda guarda mi nombre como {string}")
  public void theStoreSavesMyFirstNameAs(String expectedFirstName) {
    accountDetailsTasks.openAccountDetails();
    accountDetailsQuestions.verifyChangeWasSaved(
      expectedFirstName, accountDetailsTasks.getCurrentFirstName());
  }

  /**
   * Restores the original value so the scenario can run again without leaving the
   * customer data altered.
   */
  @Y("restauro mi nombre original")
  public void restoreMyOriginalFirstName() {
    String originalFirstName = getTestContext().get(ORIGINAL_FIRST_NAME.name());
    accountDetailsTasks.updateFirstName(originalFirstName);
    accountDetailsTasks.openAccountDetails();
    accountDetailsQuestions.verifyChangeWasReverted(
      originalFirstName, accountDetailsTasks.getCurrentFirstName());
  }
}
