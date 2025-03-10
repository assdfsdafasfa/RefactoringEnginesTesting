package rendon.selecet.generation.prompt;

import java.util.Random;

public class RenameVariable {
	public static void main(String[] args) {
		String prompt = "Please generate five different rename local variable refactoring test case that meets the following:";
		// 0: keywords; 1：方法调用； 2， 字段调用； 3: name conflict; 4：
		Random random = new Random();// select
		int n1 = random.nextInt(18);

		String renamedMethod = "1. renaming local variable is " + modifier[n1] + " ;";
		prompt = prompt + renamedMethod;
		int n2 = random.nextInt(3);
		// 0 var 1 para 3 field
//		System.out.println("n2:" + n2);
		if (n2 == 0) {
			int n3 = random.nextInt(18);
			int n4 = random.nextInt(11);
			String context = "2. Including other variables are " + modifier[n3] + " and located at "
					+ methodPosition[n4] + " ;";
			prompt = prompt + context;
		} else if (n2 == 1) {
			int n3 = random.nextInt(18);
			String context = "2. Including other parameters are " + modifier[n3] + " ;";
			prompt = prompt + context;
		} else if (n2 == 2) {
			int n3 = random.nextInt(18);
			int n4 = random.nextInt(11);
			String context = "2. Including other fields are " + modifier[n3] + " and located at " + methodPosition[n4]
					+ " ;";
			prompt = prompt + context;
		}
		String condition = "3.  new name of the renaming variable is the name(local variable or parameter or field)  that has appeared in the context;";
		prompt = prompt + condition;
		System.out.println(prompt);
	}

	// modifier
	static String[] modifier = { "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };
	static String[] methodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };

	// conflict contain a same level identifier
	static String[] conflict = { "field", "variable", "parameter" };

	static String[] loopStructure = { "while", "do", "for", "foreach" };

	// called field position
	static String[] fieldPosition = { "current class", "parent class", "abstract class", "introduced fields",
			"Anonymous Inner Class", "inner class", "Inheriting interface classes ", "Anonymous Inner Class",
			"Annotation interface", "external class", "inheriting parent classes", "sub class" };
}
