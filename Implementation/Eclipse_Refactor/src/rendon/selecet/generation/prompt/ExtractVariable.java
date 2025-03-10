package rendon.selecet.generation.prompt;

import java.util.Random;

public class ExtractVariable {

	public static void main(String[] args) {
		String prompt = "Please generate a extract local variable refactoring test case that meets the following:";
		// 0: keywords; 1：方法调用； 2， 字段调用； 3: name conflict; 4：calculate; 5: gen;
		Random random = new Random();
		int n1 = random.nextInt(6);
		if (n1 == 0) {
			int n2 = random.nextInt(7);
			String keyString = "2. extract expressions contains: " + keys[n2] + " keyword;";
			prompt = prompt + keyString;
		} else if (n1 == 1) {
			int n3 = random.nextInt(18);
			int n4 = random.nextInt(11);
			String context = "2.  extract expressions includs other fields are " + modifier[n3] + " and located at "
					+ methodPosition[n4] + " ;";
			prompt = prompt + context;
		} else if (n1 == 2) {
			int n3 = random.nextInt(18);
			int n4 = random.nextInt(11);
			String context = "2. extract expressions  includs other fields are " + modifier[n3] + " and located at "
					+ fieldPosition[n4] + " ;";
			prompt = prompt + context;
		} else if (n1 == 3) {
			int n3 = random.nextInt(3); // var -para-field
			int n4 = random.nextInt(18);
			String context = "2.  extract expressions includs other " + identifier[n3] + " are " + modifier[n4];
			prompt = prompt + context;
		} else if (n1 == 4) {
			String context = "2.  extract expressions includs compute statement;";
			prompt = prompt + context;
		} else if (n1 == 5) {
			String context = "2. Randomly generated tests for extracting local variables and refactoring test cases that may cause compilation errors or changes in refactoring behavior;";
			prompt = prompt + context;
		}

		System.out.println(prompt);
	}

	static String[] identifier = { "field", "parameter", "local variable" };
	//
	static String[] types = { "Lambda expression", "class object statement", "method invocation", "field assg",
			"exception", "inner class", "recursion" };

	// numbers
	int[] num = { 1, 2, 3, 4 };

	// conflict
	static String[] conflicts = { "new variable conflict with other variable", "new variable conflict with field",
			"new variable conflict with parameter" };

	// keywords
	static String[] keys = { "this", "super", "break", "continue", "null", "true", "false" };

	// calculate
	static String[] calculate = { "++", "--" };

	// structure
	static String[] branchStructure = { "if", "else", "swith", };
	static String[] loopStructure = { "while", "do", "for", "foreach" };

	// field, method, 直接输出，没有返回值，
	// method type
	static String[] methodTypes = { "constructor Method", "Inherited Method", "Overridden Method", "Abstract Method",
			"Existing API Method", "Lambda Expressions", "recursion method" };
	// call method position
	static String[] methodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };

	// Field position
	static String[] fieldPosition = { "current class", "parent class", "abstract class", "introduced fields",
			"Anonymous Inner Class", "inner class", "Inheriting interface classes ", "Anonymous Inner Class",
			"Annotation interface", "external class", "inheriting parent classes" };
	// modifier
	static String[] modifier = { "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };
}
