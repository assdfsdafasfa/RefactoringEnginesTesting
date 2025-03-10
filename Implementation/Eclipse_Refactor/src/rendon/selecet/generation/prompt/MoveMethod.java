package rendon.selecet.generation.prompt;

import java.util.Random;

public class MoveMethod {
	// 选择move method所在类类型:move the class where the method is located to xxx.
	// 选择move method方法类型: method is xxx method
	// 选择move method 修饰符: Modifier is xxx
	// 选择move method 内容:
	// 选择move method 内容所在位置
	// 选择move method 内容修饰类型

	public static void main(String[] args) {
		String prompt = "Please generate five different scenarios move method refactoring test case that meets the following:";
		// conditions:
		Random random = new Random();// select 必要条件类型
		int n1 = random.nextInt(2); // selcet 方法体内容
		int n2 = random.nextInt(6);
		if (n1 == 1) {
			prompt = prompt + conditions_move_method[1];
			if (n2 == 0) {
				// select body type: 0 method invocation;
				int n3 = random.nextInt(8);
				int n4 = random.nextInt(11);
				int n5 = random.nextInt(18);
				String methodInvocation = "2. Calling " + modifier[n5] + " " + methodTypes[n3] + " of "
						+ methodPosition[n4] + " within the method body;";
				prompt = prompt + methodInvocation;
			} else if (n2 == 1) {
				// 1 field quote;
				int n6 = random.nextInt(10);
				int n7 = random.nextInt(18);
				String fieldInvocation = "2. Calling " + modifier[n7] + " field of " + fieldPosition[n6]
						+ " within the method body;";
				prompt = prompt + fieldInvocation;
			} else if (n2 == 2) {
				// 2 keywords quote;
				int n8 = random.nextInt(2);
				String keyString = "2. contain " + specificKeywords[n8] + " keyword reference;";
				prompt = prompt + keyString;
			} else if (n2 == 3) {
				// 3 name conflict
				int n9 = random.nextInt(2);
				prompt = prompt + conficts[n9];
			} else if (n2 == 4) {
				// confict name
				int n10 = random.nextInt(11);
				int n11 = random.nextInt(18);
				String calledMethod = "2. move method called by " + modifier[n11] + " " + methodTypes[n10];
				prompt = prompt + calledMethod;
			} else if (n2 == 5) {
				// 5 random generate
				String body = "2. content of randomly generated method body;";
				prompt = prompt + body;
			}
			int n12 = random.nextInt(18);
			String moveModifier = "3. move method modifier is " + modifier[n12];
			prompt = prompt + moveModifier;
			System.out.println(prompt);
		} else {
			prompt = prompt + conditions_move_method[2];
			if (n2 == 0) {
				// select body type: 0 method invocation; 1 field quote; 2
				// keywords quote; 3
				// name conflict ;4 called method; 5 random generate
				int n3 = random.nextInt(8);
				int n4 = random.nextInt(11);
				int n5 = random.nextInt(18);
				String methodInvocation = "2. Calling " + modifier[n5] + " " + methodTypes[n3] + " of "
						+ methodPosition[n4] + " within the method body;";
				prompt = prompt + methodInvocation;
			} else if (n2 == 1) {
				int n6 = random.nextInt(10);
				int n7 = random.nextInt(18);
				String fieldInvocation = "2. Calling " + modifier[n7] + " field of " + fieldPosition[n6]
						+ " within the method body;";
				prompt = prompt + fieldInvocation;
			} else if (n2 == 2) {
				int n8 = random.nextInt(2);
				String keyString = "2. contain " + specificKeywords[n8] + " keyword reference;";
				prompt = prompt + keyString;
			} else if (n2 == 3) {
				int n9 = random.nextInt(2);
				prompt = prompt + conficts[n9];
			} else if (n2 == 4) {
				int n10 = random.nextInt(8);
				int n11 = random.nextInt(18);
				String calledMethod = "2. move method called by " + modifier[n11] + " " + methodTypes[n10];
				prompt = prompt + calledMethod;
			} else if (n2 == 5) {
				String body = "2. content of randomly generated method body;";
				prompt = prompt + body;
			}
			int n12 = random.nextInt(18);
			String moveModifier = "3. move method modifier is " + modifier[n12];
			prompt = prompt + moveModifier;
			System.out.println(prompt);
		}
	}

	// necessary condition
	static String[] conditions_move_method = { "move method is static method",
			"1. move method parameter contians the target class parameter type;",
			"1. class of the move method contains a field of the target class type;" };
	// move method position
	static String[] movemethodPosition = { "inner class", "common class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface class",
			"external class", "enum class", "inheriting parent class", "sub class" };
	// modifier
	static String[] modifier = { "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };
	// method type
	static String[] methodTypes = { "constructor Method", "Inherited Method", "Overridden Method", "Abstract Method",
			"Lambda Expressions", "recursion method", "common method", "Implementation method" };
	// call method position
	static String[] methodPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };

	// Field position
	static String[] fieldPosition = { "current class", "parent class", "abstract class", "introduced fields",
			"inner class", "Inheriting interface classes ", "Anonymous Inner Class", "Annotation interface",
			"external class", "inheriting parent classes" };

	// Name Conflict
	static String[] conficts = { "2. Calling a method with the same name as a method in the target class;",
			"2. Move method name is consistent with a method name in the target class;" };

	// Self type
	static String[] specificKeywords = { "this", "super" };

	// recursion
	static String[] features = { "constructor Method", "Inherited Method", "Overridden Method", "Abstract Method",
			"Existing API Method", "recursion method" };
	// target class
	static String[] targetClassPosition = { "inner class", "current class", "abstract class", "Interface class",
			"Inheriting interface classes ", "Anonymous Inner Class", "Lambda expression", "Annotation interface",
			"external class", "enum class", "inheriting parent classes" };

}
