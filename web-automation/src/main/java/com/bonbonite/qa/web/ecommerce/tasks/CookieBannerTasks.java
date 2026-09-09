package com.bonbonite.qa.web.ecommerce.tasks;

import static com.bonbonite.qa.web.actions.CommonActions.click;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementInvisible;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.ecommerce.enums.ElementDescriptions.COOKIE_REJECT_BUTTON;

import com.bonbonite.qa.web.ecommerce.pages.components.CookieBannerComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Acciones sobre el banner de consentimiento de cookies.
 */
@Slf4j
public class CookieBannerTasks extends CookieBannerComponent {

  private static final int BANNER_TIMEOUT = 10;

  /**
   * Rechaza las cookies opcionales si el banner está presente.
   *
   * <p>El banner no siempre aparece: depende de si el navegador conserva el
   * consentimiento de una sesión anterior. Por eso la ausencia del banner no es un
   * error, y el escenario continúa con normalidad.</p>
   */
  @Step("Rechazar las cookies opcionales")
  public void rejectOptionalCookies() {
    if (!isTheElementVisible(getLblCookieBanner(), BANNER_TIMEOUT)) {
      log.info("El banner de cookies no se mostró; la sesión ya tenía consentimiento");
      return;
    }
    click(getBtnRejectAll(), COOKIE_REJECT_BUTTON.getValue());
    isTheElementInvisible(getLblCookieBanner(), BANNER_TIMEOUT);
  }
}
