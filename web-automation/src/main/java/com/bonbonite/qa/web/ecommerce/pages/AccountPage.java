package com.bonbonite.qa.web.ecommerce.pages;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Localizadores de la pantalla de cuenta, que aloja tanto el formulario de inicio
 * de sesión como el de registro.
 *
 * <p>La tienda identifica al cliente por su número de cédula y no por el correo,
 * de ahí que el campo de usuario se nombre como documento en toda la automatización.
 * Los dos formularios conviven en la misma dirección y se alternan con el enlace
 * "Regístrate", por lo que se modelan en una sola pantalla.</p>
 */
@Getter
public class AccountPage extends WebBaseScreen {

  @FindBy(id = "username")
  private WebElement txtLoginDocumentNumber;

  @FindBy(id = "password")
  private WebElement txtLoginPassword;

  @FindBy(id = "rememberme")
  private WebElement chkRememberMe;

  @FindBy(css = "button[name='login']")
  private WebElement btnSignIn;

  @FindBy(css = ".woocommerce-error li")
  private WebElement lblErrorMessage;

  @FindBy(id = "show_register")
  private WebElement lnkShowRegister;

  @FindBy(id = "form-register")
  private WebElement frmRegister;

  @FindBy(id = "reg_username")
  private WebElement txtRegisterDocumentNumber;

  @FindBy(id = "first_name")
  private WebElement txtFirstName;

  @FindBy(id = "last_name")
  private WebElement txtLastName;

  @FindBy(id = "reg_email")
  private WebElement txtEmail;

  @FindBy(id = "reg_password")
  private WebElement txtRegisterPassword;

  @FindBy(id = "reg_password2")
  private WebElement txtConfirmPassword;

  @FindBy(id = "newsletter_authorization")
  private WebElement chkNewsletter;

  @FindBy(id = "privacy_policy_reg")
  private WebElement chkPrivacyPolicy;

  @FindBy(css = "button[name='register']")
  private WebElement btnRegister;
}
