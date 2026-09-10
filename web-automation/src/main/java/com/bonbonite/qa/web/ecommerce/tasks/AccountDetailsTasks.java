package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.clickHandlingInterception;
import static com.bonbonite.qa.web.actions.CommonActions.getText;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementInvisible;
import static com.bonbonite.qa.web.actions.CommonActions.scrollToElement;
import static com.bonbonite.qa.web.actions.CommonActions.sendKeys;
import static com.bonbonite.qa.web.driver.BrowserProperties.ACCOUNT_URL;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.ACCOUNT_FIRST_NAME_INPUT;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.ACCOUNT_SAVE_BUTTON;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.ACCOUNT_UPDATE_BUTTON;

import com.bonbonite.qa.api.exceptions.CustomException;
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

  /** Seconds allowed for the inline editor to save and close. */
  private static final int SAVE_TIMEOUT = 20;

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
    return getText(getLblFirstNameValue(), ACCOUNT_FIRST_NAME_INPUT.getValue());
  }

  /**
   * Replaces the first name of the account and saves the change.
   *
   * @param firstName new value for the first name
   */
  @Step("Cambiar el nombre de la cuenta a {firstName}")
  public void updateFirstName(String firstName) {
    scrollToElement(getBtnUpdateInfo(), ACCOUNT_UPDATE_BUTTON.getValue());
    clickHandlingInterception(getBtnUpdateInfo(), ACCOUNT_UPDATE_BUTTON.getValue());
    sendKeys(getTxtFirstName(), firstName, ACCOUNT_FIRST_NAME_INPUT.getValue());
    clickHandlingInterception(getBtnSaveChanges(), ACCOUNT_SAVE_BUTTON.getValue());
    awaitEditorClosed();
  }

  /**
   * Waits until the inline editor closed after saving.
   *
   * <p>The theme saves the profile with JavaScript and only then puts the screen back
   * into read only mode. Navigating away before that happens cancels the request in
   * flight, and the change is silently lost.</p>
   *
   * @throws CustomException if the editor never returns to read only mode
   */
  private void awaitEditorClosed() {
    if (!isTheElementInvisible(getBtnSaveChanges(), SAVE_TIMEOUT)) {
      throw new CustomException(String.format(
        "El editor de la cuenta no se cerró después de %d segundos, "
          + "el cambio pudo no haberse guardado", SAVE_TIMEOUT));
    }
  }
}
