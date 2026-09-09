# language: es
@web @cuenta @sesion
Característica: Inicio de sesión

  Como cliente registrado de Bon-bonite
  quiero iniciar sesión con mi número de cédula
  para poder comprar y administrar mis datos.

  Antecedentes:
    Dado que ingreso a la tienda con las cookies rechazadas
    Y que estoy en la pantalla de mi cuenta

  @smoke @credenciales
  Escenario: El cliente registrado accede a su cuenta
    Cuando inicio sesión con el cliente registrado
    Entonces la tienda me reconoce como cliente autenticado

  @regresion @negativo
  Escenario: La tienda rechaza una contraseña incorrecta
    Cuando intento iniciar sesión con la contraseña incorrecta
    Entonces la tienda rechaza el acceso e informa el error

  @regresion @negativo
  Escenario: La tienda rechaza una cédula no registrada
    Cuando intento iniciar sesión con credenciales inexistentes
    Entonces la tienda rechaza el acceso e informa el error

  @regresion @negativo
  Escenario: La tienda exige los campos obligatorios
    Cuando envío el formulario de inicio de sesión vacío
    Entonces la tienda rechaza el acceso e informa el error
