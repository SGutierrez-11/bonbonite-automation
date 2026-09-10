package runner;

import java.util.concurrent.atomic.AtomicInteger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Runs a scenario a second time when it failed for an infrastructure reason.
 *
 * <p>The policy is deliberately narrow, because a retry that hides a real defect is
 * worse than a red build. The rule is the type of the failure:</p>
 *
 * <ul>
 *   <li><b>It retries</b> when the cause is the environment — the site blocked the
 *       request, an element never rendered, the session dropped. Those failures say
 *       nothing about the quality of the product.</li>
 *   <li><b>It never retries</b> an {@link AssertionError}. That is a validation that
 *       did not hold, which is exactly what the suite exists to detect.</li>
 * </ul>
 *
 * <p>A retried scenario stays visible in the report: the first attempt is recorded
 * with its evidence, so nobody can mistake an unstable run for a clean one.</p>
 */
public class RetryAnalyzer implements IRetryAnalyzer {

  private static final int MAX_RETRIES = 1;

  private final AtomicInteger attempts = new AtomicInteger();

  @Override
  public boolean retry(ITestResult result) {
    if (attempts.get() >= MAX_RETRIES) {
      return false;
    }
    if (isFunctionalFailure(result.getThrowable())) {
      return false;
    }
    attempts.incrementAndGet();
    System.out.printf("Reintentando '%s' por un fallo de entorno: %s%n",
      result.getName(), describe(result.getThrowable()));
    return true;
  }

  /**
   * Tells whether the failure is a validation that did not hold.
   *
   * <p>The whole cause chain is inspected, because an assertion raised inside a task
   * arrives wrapped in another exception.</p>
   *
   * @param failure throwable that ended the scenario, may be null
   * @return {@code true} when the failure represents a defect of the product
   */
  private static boolean isFunctionalFailure(Throwable failure) {
    for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
      if (cause instanceof AssertionError) {
        return true;
      }
      if (cause == cause.getCause()) {
        break;
      }
    }
    return false;
  }

  private static String describe(Throwable failure) {
    if (failure == null) {
      return "sin detalle";
    }
    String message = failure.getMessage();
    return failure.getClass().getSimpleName()
      + (message == null ? "" : " - " + message.lines().findFirst().orElse(""));
  }
}
