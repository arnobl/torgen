package torgen;

import java.lang.annotation.Annotation;
import org.junit.Test;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;

public class NoTestProcessor extends AbstractProcessor<CtMethod<?>> {
	private CtType<Annotation> testType = null;

	@Override
	public boolean isToBeProcessed(final CtMethod<?> method) {
		// A test method is usually public, non-static, void, and with a name starting with 'test'.
		return isTestMethod(method) &&
			// Checking whether the test method has an @Test annotation.
			method.getAnnotations().stream().noneMatch(anot -> anot.equals(getTestType()));
	}

	public static boolean isTestMethod(final CtMethod<?> method) {
		return method != null && method.getVisibility() == ModifierKind.PUBLIC &&
			method.getFactory().Type().voidPrimitiveType().equals(method.getType()) &&
			!method.getModifiers().contains(ModifierKind.STATIC) &&
			method.getSimpleName().startsWith("test");
	}

	private CtType<Annotation> getTestType() {
		// Loading the annotation type corresponding to the @Test JUnit annotation.
		if(testType == null) {
			testType = getFactory().Annotation().getAnnotationType(Test.class.getTypeName());
		}
		return testType;
	}

	@Override
	public void process(final CtMethod<?> method) {
		// Refactoring the code by adding the missing annotation.
		method.getFactory().Annotation().annotate(method, Test.class);
	}
}
