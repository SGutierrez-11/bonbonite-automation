package com.bonbonite.qa.web.ecommerce.pages.components;

import com.bonbonite.qa.web.pages.WebBaseComponent;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Cookie consent banner.
 *
 * <p>It shows up on every fresh session and covers part of the page, so it gets in
 * the way of any scenario that starts by navigating. It is modelled as a component
 * because it belongs to no screen in particular: it may appear on any of them.</p>
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
