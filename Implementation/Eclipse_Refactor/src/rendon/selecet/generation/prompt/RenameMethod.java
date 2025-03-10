package rendon.selecet.generation.prompt;

import java.util.Random;

public class RenameMethod {

	public static void main(String[] args) {
		String prompt = "Please generate five different rename method refactoring test case that meets the following:";
		// 0: keywords; 1：方法调用； 2， 字段调用； 3: name conflict; 4：random gen
		Random random = new Random();// select
		int n1 = random.nextInt(18);
		int n2 = random.nextInt(11);

		String renamedMethod = "1. " + modifier[n1] + " renaming method is located at " + methodPosition[n2] + " ;";
		prompt = prompt + renamedMethod;
		int n3 = random.nextInt(18);
		int n4 = random.nextInt(11);

		String context = "2. Including other methods is " + modifier[n3] + " and located at " + methodPosition[n4]
				+ " ;";
		prompt = prompt + context;

		String condition = "3.  new name of the renaming method is the method name that has appeared in the context before;";
		prompt = prompt + condition;
		System.out.println(prompt);
	}

	// modifier
	static String[] modifier = { "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };

	//
	static String[] methodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };

	int[] num = { 1, 2, 3, 4 };

	// a same level method
}
