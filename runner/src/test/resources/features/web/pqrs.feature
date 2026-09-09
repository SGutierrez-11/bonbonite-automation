# language: es
@web @pqrs
Característica: Peticiones, quejas, reclamos y sugerencias

  Como cliente de Bon-bonite
  quiero radicar una solicitud de servicio al cliente
  para recibir respuesta sobre mi caso.

  El escenario positivo se detiene antes de enviar. Enviarlo abriría una solicitud
  real en la mesa de servicio del cliente, lo cual está fuera del alcance acordado.

  Antecedentes:
    Dado que ingreso a la tienda con las cookies rechazadas
    Y que estoy en el formulario de PQRS

  @regresion
  Escenario: El formulario acepta una solicitud completa
    Cuando diligencio la solicitud con la descripción "Consulta sobre el estado de mi pedido"
    Entonces la solicitud queda lista para enviarse

  @regresion @negativo
  Escenario: El formulario exige los campos obligatorios
    Cuando envío la solicitud sin diligenciar ningún campo
    Entonces la tienda informa los campos obligatorios y no crea la solicitud
