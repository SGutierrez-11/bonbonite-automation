package com.bonbonite.qa.api.context;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Da acceso al contexto del escenario en ejecución.
 *
 * <p>Mantiene una instancia de {@link Context} por hilo, de modo que cada escenario
 * trabaje con sus propios datos aunque la suite corra en paralelo. El hook de cierre
 * es responsable de invocar {@link #cleanTestContext()} al terminar el escenario.</p>
 */
@Slf4j
@UtilityClass
public class TestContextManager {

  private static final ThreadLocal<Context> TEST_CONTEXT = new ThreadLocal<>();

  /**
   * Devuelve el contexto del hilo actual, creándolo si aún no existe.
   *
   * @return contexto del escenario en ejecución
   */
  public static Context getTestContext() {
    if (TEST_CONTEXT.get() == null) {
      TEST_CONTEXT.set(Context.nonThreadSafeContext());
    }
    return TEST_CONTEXT.get();
  }

  /**
   * Descarta el contexto del hilo actual para que el siguiente escenario
   * no herede datos del anterior.
   */
  public static void cleanTestContext() {
    log.debug("Limpiando el contexto del escenario");
    TEST_CONTEXT.remove();
  }
}
