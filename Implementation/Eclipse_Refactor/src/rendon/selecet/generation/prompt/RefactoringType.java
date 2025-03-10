package rendon.selecet.generation.prompt;

public class RefactoringType {
	public String[] refactoringType = { "rename method refactoring, ", "rename local variable refactoring, ",
			"rename field refactoring, ", "inline method refactoring, ", "move method refactoring, ",
			"extract method refactoring, ", "extract variable refactoring, " };
	public String[] numbersOfClasses = { "2", "3", "4" };

	public String[] accessModifiers = { "public, ", "protected, ", "private, ", "default, ", "" };

	public String[] noAccessModifiers = { "abstract, ", "static, ", "synchronized, ", "native, ", "transient, ",
			"final, ", " " };

	String[] isRewrite = { "true", "false" };

	String[] isReload = { "true", "false" };

	String[] isSerialization = { "true", "false" };

	String[] isLambad = { "true", "false" };

	String[] isNestedClass = { "true", "false" };

	String[] isCalled = { "true", "false" };

	String[] isRecursion = { "true", "false" };

	String[] isGeberics = { "true", "false" };

	String[] isInnerClass = { "true", "false" };

	String[] isWildcardCharacter = { "true", "false" };

	String[] isAssign = { "true", "false" };

	String[] isComputed = { "true", "false" };

	String[] isMultithreading = { "true", "false" };

	String[] isInterface = { "true", "false" };

	String[] isBranchStructure = { "true", "false" };

	String[] branchs = { "if_else", "while", "switch" };

}
