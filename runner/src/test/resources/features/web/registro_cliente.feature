# language: es
@web @cuenta @registro
Característica: Registro de cliente

  Como visitante de Bon-bonite
  quiero crear una cuenta con mis datos
  para poder comprar en la tienda.

  El escenario se detiene antes de enviar el formulario. Enviarlo crearía un usuario
  real en la base de datos de producción del cliente, lo cual está fuera del alcance
  acordado.

  Antecedentes:
    Dado que ingreso a la tienda con las cookies rechazadas
    Y que estoy en la pantalla de mi cuenta

  @regresion
  Escenario: El formulario de registro acepta datos válidos
    Cuando abro el formulario de registro
    Y diligencio el registro con datos válidos
    Entonces el formulario de registro queda listo para enviarse
