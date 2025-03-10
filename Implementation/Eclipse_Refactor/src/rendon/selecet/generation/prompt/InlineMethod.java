package rendon.selecet.generation.prompt;

import java.util.Random;

public class InlineMethod {

	public static void main(String[] args) {
		String prompt = "Please generate five different scenarios inline method refactoring test case that meets the following:";
		Random random = new Random();
		int n1 = random.nextInt(18);
		int n2 = random.nextInt(11);
		// pos, mod
		String methodModifier = "1. inline method modifier is " + modifier[n1] + ", method of " + movemethodPosition[n2]
				+ " ;";
		prompt = prompt + methodModifier;
		// 0: keywords; 1: name conflict; 2: calling; 3: random gen body
		int n3 = random.nextInt(5);
		System.out.println("n3:" + n3);
		if (n3 == 0) {
			int n4 = random.nextInt(2);
			String keyString = "2. contain '" + state[n4] + "' keyword reference;";
			prompt = prompt + keyString;
		} else if (n3 == 1) {
			int n5 = random.nextInt(9);
			String conflict = "2. " + conflicts[n5] + " ;";
			prompt = prompt + conflict;
		} else if (n3 == 2) {
			int n4 = random.nextInt(7);
			int n5 = random.nextInt(11);
			int n6 = random.nextInt(18);
			String methodInvocation = "2. Calling " + modifier[n6] + " " + methodTypes[n4] + " of " + methodPosition[n5]
					+ " within the method body;";
			prompt = prompt + methodInvocation;
		} else if (n3 == 3) {
			int n6 = random.nextInt(10);
			int n7 = random.nextInt(18);
			String fieldInvocation = "2. Calling " + modifier[n7] + " field of " + fieldPosition[n6]
					+ " within the method body;";
			prompt = prompt + fieldInvocation;
		} else if (n3 == 4) {
			String body = "2. Randomly generate code that may cause inline method refactoring compile errors or expected behavior errors;";
			prompt = prompt + body;
		}
		System.out.println(prompt);
	}

	// modifier
	static String[] modifier = { "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };

	// call method;
	static String[] state = { "super", "this" };

	// scr method body 内容与Inline method内容冲突
	static String[] conflicts = { "Inline methods and calling method contain the same variable declaration",
			"Inline methods and calling methods contain the same inner class",
			"Inline methods and calling methods contain the same class object",
			"Inline methods and calling methods contain the same interface class",
			"Inline methods and calling methods contain the same Lambda expression",
			"Inline methods and calling methods contain the same enum class",
			"Inline methods and calling methods contain the same annotation interface class",
			"Inline methods and calling methods contain the same inheriting parent class",
			"Inline methods and calling methods contain the same inheriting abstract class" };

	// inline method position
	static String[] movemethodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent class" };

	// inline method called;
	static String[] called = { "field", "method", "exception hand" };

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
}
