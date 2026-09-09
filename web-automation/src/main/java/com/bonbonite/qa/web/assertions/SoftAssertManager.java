package com.bonbonite.qa.web.assertions;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.testng.asserts.SoftAssert;

/**
 * Administra las aserciones suaves de cada escenario.
 *
 * <p>Una question que valida cinco campos de una pantalla debe reportar los cinco
 * resultados, no detenerse en el primero que falle: quien lea el reporte necesita
 * ver el alcance completo del defecto. Las validaciones se acumulan aquí durante el
 * escenario y el hook de cierre invoca {@link #assertAll()} para consolidarlas.</p>
 *
 * <p>Se mantiene una instancia por hilo para que la ejecución en paralelo no mezcle
 * los resultados de escenarios distintos.</p>
 */
@Slf4j
@UtilityClass
public class SoftAssertManager {

  private static final ThreadLocal<SoftAssert> SOFT_ASSERT =
    ThreadLocal.withInitial(SoftAssert::new);

  /**
   * Devuelve el acumulador de aserciones del hilo actual.
   *
   * @return instancia sobre la que se registran las validaciones
   */
  public static SoftAssert getSoftAssert() {
    return SOFT_ASSERT.get();
  }

  /**
   * Consolida las validaciones acumuladas y libera el acumulador del hilo.
   *
   * <p>Si alguna falló, lanza el error con el detalle de todas para que el escenario
   * se marque como fallido. El acumulador se libera en cualquier caso, de modo que
   * el siguiente escenario del mismo hilo empiece limpio.</p>
   */
  public static void assertAll() {
    SoftAssert softAssert = SOFT_ASSERT.get();
    try {
      softAssert.assertAll();
    } finally {
      SOFT_ASSERT.remove();
    }
  }
}
