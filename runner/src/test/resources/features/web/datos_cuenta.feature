# language: es
@web @cuenta @datos
Característica: Modificación de los datos de la cuenta

  Como cliente registrado de Bon-bonite
  quiero modificar los datos de mi perfil
  para mantener mi información al día.

  El escenario restaura el valor original antes de terminar, de modo que pueda
  ejecutarse tantas veces como se necesite sin alterar los datos del cliente.

  Antecedentes:
    Dado que ingreso a la tienda con las cookies rechazadas
    Y que estoy en la pantalla de mi cuenta
    Cuando inicio sesión con el cliente registrado
    Entonces la tienda me reconoce como cliente autenticado

  @regresion
  Escenario: El cliente actualiza su nombre y el cambio persiste
    Cuando abro los detalles de mi cuenta
    Y cambio mi nombre a "NombreDePrueba"
    Entonces la tienda guarda mi nombre como "NombreDePrueba"
    Y restauro mi nombre original
