package com.bonbonite.qa.web.pages;

import org.openqa.selenium.support.PageFactory;

/**
 * Clase base de los componentes reutilizables de la interfaz.
 *
 * <p>Un componente es un widget que aparece en varias pantallas —el banner de
 * cookies, el encabezado, el carrito lateral— y por eso no pertenece a ninguna en
 * particular. Hereda el manejo del driver de la pantalla base y vuelve a inicializar
 * Page Factory sobre sus propios campos.</p>
 */
public abstract class WebBaseComponent extends WebBaseScreen {

  /**
   * Inicializa los localizadores declarados en el componente.
   */
  protected WebBaseComponent() {
    super();
    PageFactory.initElements(getDriver(), this);
  }
}
