package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Locators of the account details screen, reachable only with an active session.
 *
 * <p>It also holds the elements that prove the session is open — the account
 * navigation and the log out link — which is what the sign in scenario asserts
 * against.</p>
 */
@Getter
public class AccountDetailsPage extends WebBaseScreen {

  @FindBy(css = "nav.woocommerce-MyAccount-navigation, .woocommerce-MyAccount-navigation")
  private WebElement lstAccountMenu;

  @FindBy(css = "a[href*='customer-logout']")
  private WebElement lnkLogout;

  @FindBy(css = "form.woocommerce-EditAccountForm")
  private WebElement frmAccountDetails;

  @FindBy(id = "account_first_name")
  private WebElement txtFirstName;

  @FindBy(id = "account_last_name")
  private WebElement txtLastName;

  @FindBy(id = "account_display_name")
  private WebElement txtDisplayName;

  @FindBy(id = "account_email")
  private WebElement txtEmail;

  @FindBy(css = "button[name='save_account_details']")
  private WebElement btnSaveChanges;

  @FindBy(css = ".woocommerce-message")
  private WebElement lblSuccessMessage;
}
