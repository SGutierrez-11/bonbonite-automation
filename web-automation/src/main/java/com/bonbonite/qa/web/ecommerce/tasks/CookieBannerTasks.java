package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.click;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementInvisible;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.COOKIE_REJECT_BUTTON;

import com.bonbonite.qa.web.ecommerce.pages.components.CookieBannerComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Actions on the cookie consent banner.
 */
@Slf4j
public class CookieBannerTasks extends CookieBannerComponent {

  private static final int BANNER_TIMEOUT = 10;

  /**
   * Rejects the optional cookies when the banner is present.
   *
   * <p>The banner does not always show up: it depends on whether the browser keeps
   * the consent from a previous session. Its absence is therefore not an error and
   * the scenario carries on normally.</p>
   */
  @Step("Rechazar las cookies opcionales")
  public void rejectOptionalCookies() {
    if (!isTheElementVisible(getLblCookieBanner(), BANNER_TIMEOUT)) {
      log.info("The cookie banner was not displayed; the session already had consent");
      return;
    }
    click(getBtnRejectAll(), COOKIE_REJECT_BUTTON.getValue());
    isTheElementInvisible(getLblCookieBanner(), BANNER_TIMEOUT);
  }
}
