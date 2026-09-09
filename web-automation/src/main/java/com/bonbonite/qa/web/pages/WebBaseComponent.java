package com.bonbonite.qa.web.pages;

import org.openqa.selenium.support.PageFactory;

/**
 * Base class of the reusable interface components.
 *
 * <p>A component is a widget that shows up on several screens — the cookie banner,
 * the header, the mini cart — and therefore belongs to none of them in particular.
 * It inherits driver handling from the base screen and initializes Page Factory
 * again over its own fields.</p>
 */
public abstract class WebBaseComponent extends WebBaseScreen {

  /**
   * Initializes the locators declared by the component.
   */
  protected WebBaseComponent() {
    super();
    PageFactory.initElements(getDriver(), this);
  }
}
