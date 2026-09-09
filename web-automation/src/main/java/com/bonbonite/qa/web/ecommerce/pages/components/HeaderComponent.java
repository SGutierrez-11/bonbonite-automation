package com.bonbonite.qa.web.ecommerce.pages.components;

import com.bonbonite.qa.web.pages.WebBaseComponent;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Encabezado fijo del sitio, con la navegación por categorías, el acceso a la
 * cuenta y el carrito.
 *
 * <p>El sitio renderiza dos encabezados simultáneos en el documento: uno para
 * escritorio, contenido en un bloque {@code lg:block}, y otro para móvil en un
 * bloque {@code lg:hidden}. Solo uno es visible según el ancho de la ventana, pero
 * ambos existen en el DOM y comparten clases. Por eso cada localizador se ancla de
 * forma explícita al contenedor de escritorio, que es el que corresponde al tamaño
 * de ventana configurado por defecto. Depender del orden de aparición haría que la
 * prueba fallara de manera intermitente contra un elemento oculto.</p>
 *
 * <p>Los enlaces de categoría se ubican por su URL y no por el identificador
 * numérico que genera el gestor de contenidos, porque ese identificador cambia si
 * alguien reordena el menú desde el administrador y dejaría las pruebas rotas sin
 * que el sitio hubiera cambiado para el usuario.</p>
 */
@Getter
public class HeaderComponent extends WebBaseComponent {

  private static final String DESKTOP_HEADER = "header div[class*='lg:block'] ";

  @FindBy(css = "ul#menu-categories-menu")
  private WebElement lstMainMenu;

  @FindBy(css = "ul#menu-categories-menu a[href*='/categoria-producto/zapatos-mujer/']")
  private WebElement lnkShoes;

  @FindBy(css = "ul#menu-categories-menu a[href*='/categoria-producto/bolsos-mujer/']")
  private WebElement lnkBags;

  @FindBy(css = "ul#menu-categories-menu a[href*='/categoria-producto/cinturones-mujer/']")
  private WebElement lnkBelts;

  @FindBy(css = "ul#menu-categories-menu a[href*='/categoria-producto/accesorios-mujer/']")
  private WebElement lnkAccessories;

  @FindBy(css = "ul#menu-categories-menu a[href*='/categoria-producto/outlet/']")
  private WebElement lnkOutlet;

  @FindBy(css = DESKTOP_HEADER + "a[href*='/mi-cuenta']")
  private WebElement lnkAccount;

  @FindBy(css = DESKTOP_HEADER + "a.cart-contents")
  private WebElement lnkCart;

  @FindBy(css = DESKTOP_HEADER + ".cart-contents-count")
  private WebElement lblCartCount;
}
