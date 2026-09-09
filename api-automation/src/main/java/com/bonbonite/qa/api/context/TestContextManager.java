package com.bonbonite.qa.api.context;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Gives access to the context of the running scenario.
 *
 * <p>It keeps one {@link Context} instance per thread, so that every scenario works
 * with its own data even when the suite runs in parallel. The teardown hook is
 * responsible for calling {@link #cleanTestContext()} once the scenario ends.</p>
 */
@Slf4j
@UtilityClass
public class TestContextManager {

  private static final ThreadLocal<Context> TEST_CONTEXT = new ThreadLocal<>();

  /**
   * Returns the context of the current thread, creating it when it does not exist.
   *
   * @return the context of the running scenario
   */
  public static Context getTestContext() {
    if (TEST_CONTEXT.get() == null) {
      TEST_CONTEXT.set(Context.nonThreadSafeContext());
    }
    return TEST_CONTEXT.get();
  }

  /**
   * Discards the context of the current thread so the next scenario does not inherit
   * data from the previous one.
   */
  public static void cleanTestContext() {
    log.debug("Clearing the scenario context");
    TEST_CONTEXT.remove();
  }
}
