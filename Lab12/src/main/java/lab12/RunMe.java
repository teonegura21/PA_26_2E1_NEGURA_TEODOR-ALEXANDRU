package lab12;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A custom annotation. We will discover this at runtime.
 * Anything marked with @RunMe can be invoked by our tool.
 */
@Retention(RetentionPolicy.RUNTIME)  // Must be RUNTIME to be visible via reflection
public @interface RunMe {
}
