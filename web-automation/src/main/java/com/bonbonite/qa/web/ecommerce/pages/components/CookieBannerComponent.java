package com.bonbonite.qa.web.ecommerce.pages.components;

import com.bonbonite.qa.web.pages.WebBaseComponent;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Banner de consentimiento de cookies.
 *
 * <p>Aparece en toda sesión nueva y cubre parte de la página, de modo que
 * interfiere con cualquier escenario que empiece por navegar. Se modela como
 * componente porque no pertenece a una pantalla concreta: puede aparecer en
 * cualquiera.</p>
 */
@Getter
public class CookieBannerComponent extends WebBaseComponent {

  @FindBy(id = "cookiescript_injected")
  private WebElement lblCookieBanner;

  @FindBy(id = "cookiescript_reject")
  private WebElement btnRejectAll;

  @FindBy(id = "cookiescript_accept")
  private WebElement btnAcceptAll;

  @FindBy(id = "cookiescript_close")
  private WebElement btnClose;
}
