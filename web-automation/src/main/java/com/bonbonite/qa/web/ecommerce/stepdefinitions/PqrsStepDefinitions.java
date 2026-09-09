package com.bonbonite.qa.web.ecommerce.stepdefinitions;

import static com.bonbonite.qa.api.context.TestContextManager.getTestContext;
import static com.bonbonite.qa.web.driver.BrowserProperties.BASE_URL;
import static com.bonbonite.qa.web.ecommerce.enums.Keys.NEW_CUSTOMER;

import com.bonbonite.qa.api.builders.CustomerBuilder;
import com.bonbonite.qa.api.models.request.CustomerRequest;
import com.bonbonite.qa.web.ecommerce.questions.PqrsQuestions;
import com.bonbonite.qa.web.ecommerce.tasks.PqrsTasks;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

/**
 * Steps of the customer service request module.
 */
public class PqrsStepDefinitions extends WebBaseScreen {

  private static final String PQRS_PATH = "pqrs/";

  private final PqrsTasks pqrsTasks = new PqrsTasks();
  private final PqrsQuestions pqrsQuestions = new PqrsQuestions();

  /**
   * Opens the customer service request screen and checks the form is offered.
   */
  @Dado("que estoy en el formulario de PQRS")
  public void iAmOnThePqrsForm() {
    openPage(BASE_URL + PQRS_PATH);
    pqrsTasks.awaitFormIsRendered();
    pqrsQuestions.verifyFormIsDisplayed();
  }

  /**
   * Fills the request with generated contact data and a description.
   *
   * @param description text of the request
   */
  @Cuando("diligencio la solicitud con la descripción {string}")
  public void fillTheRequestWithTheDescription(String description) {
    CustomerRequest customer = CustomerBuilder.randomCustomer();
    getTestContext().set(NEW_CUSTOMER.name(), customer);
    pqrsTasks.fillRequest(customer, description);
  }

  /**
   * Verifies that the request is complete and ready to be sent, which is the agreed
   * limit for this scenario: sending it would open a real request in the client's
   * customer service queue.
   */
  @Entonces("la solicitud queda lista para enviarse")
  public void theRequestIsReadyToSubmit() {
    CustomerRequest customer = getTestContext().get(NEW_CUSTOMER.name());
    pqrsQuestions.verifyRequestIsReadyToSubmit(customer.getEmail());
  }

  /**
   * Submits the request with no data, to exercise the mandatory field validation.
   */
  @Cuando("envío la solicitud sin diligenciar ningún campo")
  public void submitTheEmptyRequest() {
    pqrsTasks.submitEmptyRequest();
  }

  /**
   * Verifies that the site refuses the empty request and reports what is missing.
   */
  @Entonces("la tienda informa los campos obligatorios y no crea la solicitud")
  public void theStoreReportsTheMandatoryFields() {
    pqrsQuestions.verifyMandatoryFieldsAreValidated();
  }
}
