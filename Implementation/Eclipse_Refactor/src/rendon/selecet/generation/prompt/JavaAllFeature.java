package rendon.selecet.generation.prompt;

public class JavaAllFeature {

	String[] classStructure = { "interface", "abstract", "enum", "class" };
	String[] innerClassStructure = { "inner class", "local calss", "class" };

	// Syntax
	String[] modifier = { "public", "private", "protected", "default", "static", "abstract", "final", "synchronized",
			" native", "strictfp", "public static", " private static", "protected static", "public abstract",
			"protected abstract", "public final", "private final", "synchronized native", "static final" };
	String[] variableTypes = { "int", "byte", "char", "long", "float", "short", "double", "boolean" };

	// structure
	String[] branchStructure = { "if", "else", "swith", };
	String[] loopStructure = { "while", "do", "for", "foreach" };
	String[] loopStructureKeywords = { "break", "contiue" };
	String[] dataStructure = { "Array", "List", "Set", "Map", "Stack", "Queue", "Heap", "Hashtable" };
	String[] hierarchyStructure = { "extends", "implements" };

	// specific method invacation
	String[] numberClassInvocation = { "compareTo", "equals", "valueOf", "toString", "parseInt", "abs" };
	String[] mathClassInvocation = { "Math", "cell", "floor", "round", "min", "max", "exp", "log", "pow", "sqrt", "sin",
			"cos", "tan", "asin", "acos", "atan", "toDegrees", "toRadians", "random" };
	String[] characaterInvocation = { "isLetter", "isDigit", "isWhitespace", "isUpperCase", "isLowerCase",
			"toUpperCase", "toLowerCase" };
	String[] stringInvocation = { "valueOf", "trim", "toUpperCase", "toLowerCase", "substring", "startsWith", "split",
			"replace", "matches", "length", "indexOf", "hashCode", "getBytes", "endsWith", "equals", "concat",
			"compareTo", "charAt" };
	String[] specificClass = { "Date", "Pattern", "Matcher", "File", "Scanner", "Exception" };

	// object oriented
	// java features
	String[] javaFeatures = { "override super method", "overload: containsSame method name, but different parameters" };

	// invocation
	String[] methods = { "Method call", "object call", "static call", "constructor call", "recursive call" };
	String[] fields = { "Fields in the current class", "fields in the parent class", "introduced fields",
			"statically imported fields", "fields in external classes" };

	// 静态分析与Gpt相结合的方式： 可以存在无限变换
	// why: 分析错误原因->ast
	// What：什么特征引起->ast
	// whose: 特征属于哪中类别 -> 认为构建基于java官方规范文档
	// how： 特征自身存在哪些变换 -> gpt
	// where: 特征可以存在什么位置 -> gpt
	// which： 选择哪些特征

}
