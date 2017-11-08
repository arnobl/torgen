package torgen;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;

public class TryCatchProcessor extends AbstractProcessor<CtTry> {
	@Override
	public boolean isToBeProcessed(final CtTry ctTry) {
		// The method must be a test method
		return NoTestProcessor.isTestMethod(ctTry.getParent(CtMethod.class)) &&
		// To refactor the code, all the catch blocks must contain a 'fail' method call.
			ctTry.getCatchers().stream().noneMatch(cat -> cat.getElements((CtInvocation<?> invok) -> isFailCall(invok)).isEmpty());
	}

	private boolean isFailCall(final CtInvocation<?> invok) {
		// The fail method has no parameter and is called 'fail'
		return invok.getExecutable().getSimpleName().equals("fail") && invok.getArguments().isEmpty();
	}

	@Override
	public void process(final CtTry ctTry) {
		// Getting the method that contains the try/catch block.
		final CtExecutable<?> exec = ctTry.getParent(CtExecutable.class);
		// replacing the try/catch by the content of the try (be careful, the content of the catch is discarded).
		ctTry.replace(ctTry.getBody().getStatements());
		// Adding the exception to the prototype of the method.
		ctTry.getCatchers().forEach(cat -> exec.addThrownType(cat.getParameter().getReference().getType()));
	}
}
