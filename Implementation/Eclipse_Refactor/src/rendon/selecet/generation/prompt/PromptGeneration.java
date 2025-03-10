package rendon.selecet.generation.prompt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PromptGeneration {
	static List<String[]> listOfArrays = new ArrayList<>();

	public static void main(String[] args) {
		addArrays();
		String generatedPropmt = generateRandomPrompt();
		System.out.println(generatedPropmt);
	}

	private static String generateRandomPrompt() {
		String prompt = "";
		String javaCharacter = "";
		List<String> selectedElements = new ArrayList<>();
		for (String[] currentArray : listOfArrays) {
			Random random = new Random();
			String selectedElement = currentArray[random.nextInt(currentArray.length)];
			selectedElements.add(selectedElement);
		}

		String refactoringType = selectedElements.get(0);
		String accessModifiers = selectedElements.get(1);
		String numbersOfClasses = selectedElements.get(2);
		String assign = selectedElements.get(3);
		String noAccessModifiers = selectedElements.get(17);
		if (assign.equals("true")) {
			javaCharacter += "assignment statement,";
		}
		String called = selectedElements.get(4);
		if (called.equals("true")) {
			javaCharacter += "multiple call,";
		}
		String computed = selectedElements.get(5);
		if (computed.equals("true")) {
			javaCharacter += "numerical calculation,";
		}
		String geberics = selectedElements.get(6);
		if (geberics.equals("true")) {
			javaCharacter += "generics,";
		}
		String innerClass = selectedElements.get(7);
		if (innerClass.equals("true")) {
			javaCharacter += "inner class,";
		}
		String interfaces = selectedElements.get(8);
		if (interfaces.equals("true")) {
			javaCharacter += "interface,";
		}
		String lambad = selectedElements.get(9);
		if (lambad.equals("true")) {
			javaCharacter += "lambad expression,";
		}
		String multithreading = selectedElements.get(10);
		if (multithreading.equals("true")) {
			javaCharacter += "multi threading,";
		}
		String nestedClass = selectedElements.get(11);
		if (nestedClass.equals("true")) {
			javaCharacter += "nested class,";
		}
		String recursion = selectedElements.get(12);
		if (recursion.equals("true")) {
			javaCharacter += "recursion,";
		}
		String reload = selectedElements.get(13);
		if (reload.equals("true")) {
			javaCharacter += "reload,";
		}
		String rewrite = selectedElements.get(14);
		if (rewrite.equals("true")) {
			javaCharacter += "rewrite,";
		}
		String serialization = selectedElements.get(15);
		if (serialization.equals("true")) {
			javaCharacter += "serialization,";
		}
		String wildcardCharacter = selectedElements.get(16);
		if (wildcardCharacter.equals("true")) {
			javaCharacter += "wildcardCharacter";
		}

		prompt = Constant.header + refactoringType + Constant.modifier + accessModifiers + noAccessModifiers
				+ Constant.numbersOfClass + numbersOfClasses + javaCharacter
				+ " these structures and properties act on a method or a class";
		return prompt;
	}

	public static void addArrays() {
		RefactoringType type = new RefactoringType();
		listOfArrays.add(type.refactoringType);
		listOfArrays.add(type.accessModifiers);
		listOfArrays.add(type.numbersOfClasses);
		listOfArrays.add(type.isAssign);
		listOfArrays.add(type.isCalled);
		listOfArrays.add(type.isComputed);
		listOfArrays.add(type.isGeberics);
		listOfArrays.add(type.isInnerClass);
		listOfArrays.add(type.isInterface);
		listOfArrays.add(type.isLambad);
		listOfArrays.add(type.isMultithreading);
		listOfArrays.add(type.isNestedClass);
		listOfArrays.add(type.isRecursion);
		listOfArrays.add(type.isReload);
		listOfArrays.add(type.isRewrite);
		listOfArrays.add(type.isSerialization);
		listOfArrays.add(type.isWildcardCharacter);
		listOfArrays.add(type.noAccessModifiers);
	}
}
