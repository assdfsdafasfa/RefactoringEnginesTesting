package rendon.selecet.generation.prompt;

import java.util.Random;

public class ExtractMethod {
	public static void main(String[] args) {
		String prompt = "Please generate five different extract method refactoring test case that meets the following:";
		Random random = new Random();
		int n1 = random.nextInt(18);
		// 0: name conflict; 1: contian class ; 2: contian key; 3 branch; 4：exception 5:
		// num invocation ; 6: recursion type ; 7. for ; 8: random gen; 9: Random
		// combination
		String moveModifier = "1. method modifier for being extracted is " + modifier[n1];
		prompt = prompt + moveModifier;
		int n2 = random.nextInt(10);
		if (n2 == 0) {
			prompt = prompt + "2. new name of extract method conflict with other method name;";
		} else if (n2 == 1) {
			int n5 = random.nextInt(11);
			String contianClass = "2. method body contains " + extractMethodPosition[n5] + " ;";
		} else if (n2 == 2) {
			int n3 = random.nextInt(5);
			String stateString = "2. extract method contian '" + state[n3] + "' keyword reference";
			prompt = prompt + stateString;
		} else if (n2 == 3) {
			int n4 = random.nextInt(2);
			prompt = prompt + branchStructure[n4];
		} else if (n2 == 4) {
			prompt = prompt + "2. contian exception handling structure;";
		} else if (n2 == 5) {
			prompt = prompt + "2. the same extraction method code snippet in multiple methods;";
		} else if (n2 == 6) {
			prompt = prompt + "2. method for being extracted is recursion method;";
		} else if (n2 == 7) {
			int n6 = random.nextInt(4);
			prompt = prompt + "2. method body contians " + loopStructure[n6] + " structure;";
		} else if (n2 == 8) {
			String body = "2. content of randomly generated method body that may cause extract method refactoring compile errors or expected behavior errors;";
			prompt = prompt + body;
		} else if (n2 == 9) {
			String s = "2. contians ";
			int n9 = random.nextInt(10);
			int n10;
			do {
				n10 = random.nextInt(10);
			} while (n10 == n9);

			if (ranString[n9].equals("method invocation")) {
				int n11 = random.nextInt(11);
				s = s + " method invocation of " + extractMethodPosition[n11];
				if (ranString[n10].equals("field invocation")) {
					int n12 = random.nextInt(11);
					s = s + " method invocation of " + extractMethodPosition[n12];
				} else {
					s = s + " and " + ranString[n10];
				}
			} else if (ranString[n10].equals("method invocation")) {
				int n11 = random.nextInt(11);
				s = s + ranString[n10] + " and " + " method invocation of " + extractMethodPosition[n11];
			} else {
				s = s + ranString[n9] + " and " + ranString[n9];
			}
			prompt = prompt + s;
		}
		System.out.println(prompt);
	}

	// extract method position
	static String[] extractMethodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };
	// extract code type;

	// same code numbers
	int[] num = { 1, 2, 3, 4 };

	// specfic statement;
	static String[] state = { "return", "break", "continue", "super", "this" };
	static String[] loopStructure = { "while", "do", "for", "foreach" };
	//
	static String[] types = { "contian exception", "recursion method",
			"new name of extract method conflict with other method name" };

	static String[] branchStructure = { "2. contain if-else structure", "2. contain swith structure" };

	// modifier
	static String[] modifier = { "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };

	// call method position
	static String[] methodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };

	// Field position
	static String[] fieldPosition = { "current class", "parent class", "abstract class", "introduced fields",
			"inner class", "Inheriting interface classes ", "Anonymous Inner Class", "Annotation interface",
			"external class", "inheriting parent classes" };

	// modifier
	static String[] ranString = { "while", "do", "for", "foreach", "exception", "if-else", "switch",
			"method invocation", "field invocation", "keywords" };
}
