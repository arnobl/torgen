package torgen;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.compiler.Environment;

public class Main {
	public static void main(final String[] args) {
		final SpoonAPI spoon = new Launcher();
		final Environment env = spoon.getEnvironment();

		env.setAutoImports(true);
		env.setComplianceLevel(8);
		env.useTabulations(true);

		spoon.addProcessor(new NoTestProcessor());
		spoon.addProcessor(new TryCatchProcessor());
		spoon.addInputResource("src/main/resources/spoon/TestBadTests.java");
		spoon.run();
	}
}
