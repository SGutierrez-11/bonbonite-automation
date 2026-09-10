package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Locators of the account details screen, reachable only with an active session.
 *
 * <p>The theme replaced the standard WooCommerce account form with an inline editor:
 * the profile is shown as read only text and only turns into fields after pressing
 * the update button, with a separate control to save. That is why the screen exposes
 * the displayed value and the field as two different elements.</p>
 *
 * <p>It also holds the proof that the session is open. The theme renders neither the
 * account navigation nor the log out link of WooCommerce, so what tells a signed in
 * customer apart is that the sign in form is no longer displayed here.</p>
 */
@Getter
public class AccountDetailsPage extends WebBaseScreen {

  @FindBy(id = "username")
  private WebElement txtLoginDocumentNumber;

  @FindBy(css = ".woocommerce-error li")
  private WebElement lblErrorMessage;

  @FindBy(id = "profile-update-form")
  private WebElement frmAccountDetails;

  @FindBy(css = "button.update-info-btn")
  private WebElement btnUpdateInfo;

  @FindBy(css = "button.save-info-btn")
  private WebElement btnSaveChanges;

  @FindBy(css = ".profile-field[data-field='first_name']")
  private WebElement lblFirstNameValue;

  @FindBy(css = "#profile-update-form [name='first_name']")
  private WebElement txtFirstName;

  @FindBy(css = ".woocommerce-message")
  private WebElement lblSuccessMessage;
}
