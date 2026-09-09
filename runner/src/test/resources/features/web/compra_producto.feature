# language: es
@web @compra
Característica: Compra de producto

  Como cliente registrado de Bon-bonite
  quiero agregar un producto a mi carrito y llegar al resumen de mi orden
  para poder completar mi compra.

  El escenario se detiene en el resumen de la orden. Registrarla crearía una orden
  real que el equipo del cliente atendería, lo cual está fuera del alcance acordado.

  Antecedentes:
    Dado que ingreso a la tienda con las cookies rechazadas
    Y que estoy en la pantalla de mi cuenta
    Cuando inicio sesión con el cliente registrado
    Entonces la tienda me reconoce como cliente autenticado

  @smoke @e2e
  Escenario: El cliente llega al resumen de su orden con el producto elegido
    Dado que abro un producto disponible de la sección "Zapatos"
    Cuando agrego el producto al carrito en la primera talla disponible
    Entonces el carrito refleja el producto y el subtotal correcto
    Cuando continúo al checkout y diligencio los datos de envío
    Entonces el resumen de la orden está completo y listo para registrarse
    Y el resumen conserva el producto seleccionado
