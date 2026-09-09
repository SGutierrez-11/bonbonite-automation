package com.bonbonite.qa.web.assertions;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.testng.asserts.SoftAssert;

/**
 * Manages the soft assertions of each scenario.
 *
 * <p>A question that validates five fields of a screen must report the five results
 * instead of stopping at the first failure: whoever reads the report needs to see
 * the full extent of the defect. Validations pile up here during the scenario and
 * the teardown hook calls {@link #assertAll()} to consolidate them.</p>
 *
 * <p>One instance per thread is kept so that parallel execution does not mix the
 * results of different scenarios.</p>
 */
@Slf4j
@UtilityClass
public class SoftAssertManager {

  private static final ThreadLocal<SoftAssert> SOFT_ASSERT =
    ThreadLocal.withInitial(SoftAssert::new);

  /**
   * Returns the assertion collector of the current thread.
   *
   * @return the instance validations are registered on
   */
  public static SoftAssert getSoftAssert() {
    return SOFT_ASSERT.get();
  }

  /**
   * Consolidates the accumulated validations and releases the thread collector.
   *
   * <p>If any of them failed, it throws the error with the detail of all of them so
   * the scenario is marked as failed. The collector is released either way, so the
   * next scenario on the same thread starts clean.</p>
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
