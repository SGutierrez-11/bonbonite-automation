package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getValue;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.CommonActions.sendKeys;
import static com.bonbonite.qa.web.driver.BrowserProperties.ACCOUNT_URL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.ACCOUNT_FIRST_NAME_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.ACCOUNT_SAVE_BUTTON;

import com.bonbonite.qa.web.ecommerce.pages.AccountDetailsPage;
import io.qameta.allure.Step;

/**
 * Actions on the account details screen.
 *
 * <p>Every scenario that changes a value here is responsible for restoring it before
 * finishing. The project runs against production with a single real account, so a
 * change left behind would alter the customer's data permanently and would make the
 * scenario non repeatable.</p>
 */
public class AccountDetailsTasks extends AccountDetailsPage {

  private static final String ACCOUNT_DETAILS_PATH = "edit-account/";

  /**
   * Opens the account details screen.
   */
  @Step("Abrir los detalles de la cuenta")
  public void openAccountDetails() {
    openPage(ACCOUNT_URL + ACCOUNT_DETAILS_PATH);
  }

  /**
   * Returns the first name currently stored in the account.
   *
   * @return the value of the first name field
   */
  public String getCurrentFirstName() {
    return getValue(getTxtFirstName(), ACCOUNT_FIRST_NAME_INPUT.getValue());
  }

  /**
   * Replaces the first name of the account and saves the change.
   *
   * @param firstName new value for the first name
   */
  @Step("Cambiar el nombre de la cuenta a {firstName}")
  public void updateFirstName(String firstName) {
    scrollToElement(getTxtFirstName(), ACCOUNT_FIRST_NAME_INPUT.getValue());
    sendKeys(getTxtFirstName(), firstName, ACCOUNT_FIRST_NAME_INPUT.getValue());
    clickHandlingInterception(getBtnSaveChanges(), ACCOUNT_SAVE_BUTTON.getValue());
  }
}
