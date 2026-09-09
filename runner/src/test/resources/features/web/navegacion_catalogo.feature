# language: es
@web @catalogo
Característica: Navegación por el catálogo

  Como visitante de la tienda
  quiero recorrer las secciones del catálogo desde el menú principal
  para encontrar los productos que me interesan.

  Antecedentes:
    Dado que ingreso a la tienda con las cookies rechazadas

  @smoke
  Escenario: El encabezado ofrece todas las secciones del catálogo
    Entonces el encabezado muestra las secciones del catálogo

  @regresion
  Esquema del escenario: Cada sección del menú abre su propio listado
    Cuando navego a la sección "<seccion>"
    Entonces el sitio muestra el listado de esa sección

    Ejemplos:
      | seccion     |
      | Zapatos     |
      | Bolsos      |
      | Cinturones  |
      | Accesorios  |
      | Outlet      |
