package runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Attaches the retry policy to every scenario of the suite.
 *
 * <p>Cucumber generates the test methods at runtime, so there is no place to put the
 * annotation by hand. This transformer sets the analyzer on each of them as TestNG
 * builds the suite.</p>
 */
@SuppressWarnings("rawtypes")
public class RetryListener implements IAnnotationTransformer {

  @Override
  public void transform(ITestAnnotation annotation, Class testClass,
                        Constructor testConstructor, Method testMethod) {
    annotation.setRetryAnalyzer(RetryAnalyzer.class);
  }
}
