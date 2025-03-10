package refactoring.random.select;

public class Constant {
	public final static String regEx = "[-\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";

	public static final String SAVE_DATA_PATH_JSON = "D:/IDEA_build/patch/refactoring/";

	public static final String CODE_SAVE_PATH_REFACTORING_AFTER = "D:/IDEA_build/patch/refactoring/";
	public static final String CODE_SAVE_PATH_REFACTORING_BEFORE = "D:/IDEA_build/patch/refactoring/";

	public static final String PYTHON_PATH = "D:/pythonProject1/venv/Scripts/python.exe";
	public static final String GPT_PATH = "D:/pythonProject1/gpt.py";

	public static final String RENAME_VARIABLE_PROMPT = " Please generate a different variable name than this one: ";
	public static final String RENAME_FIELD_PROMPT = "Please return a field name that is different from this field name: ";
	public static final String RENAME_METRHOD_PROMPT = "Please generate a different method name than this one:";
//	public static final String RENAME_METRHOD_PROMPT = " Please generate a method name for the following code： ";
	public static final String RENAME_TYPT_PROMPT = " Please generate a random class name";
	public static final String EXTRACT_VARIABLE_PROMPT = "Please generate a  local variable name for the following code：";
	public static final String EXTRACT_CONSTANT_PROMPT = "Please generate a constant name for the following code：";
	public static final String EXTRACT_METHOD_PROMPT = "Please generate a different method name for the code：";

	public static final int MAX_REFACTORING_NUMBERS = 10;
	public static final int MAX_REFACTORING_TYPES = 8;

	public static final int RENAME_REFACTORING = 0;
	public static final int EXTACT_METHOD_REFACTORING = 1;
	public static final int INLINE_METHOD_REFACTORING = 2;
	public static final int EXTACT_VARIABLE_REFACTORING = 3;
	public static final int INLINE_VARIABLE_REFACTORING = 4;
	public static final int EXTRACT_CONSTANT_REFACTORING = 5;
	public static final int INLINE_CONSTANT_REFACTORING = 6;
	public static final int ENCAPSULATE_FIELD_REFACTORING = 7;
	public static final int INVERSE_ENCAPSULATE_FIELD_REFACTORING = 8;
	public static final int CONVERT_VARIABLE_TO_FIELD_REFACTORING = 9;
//	public static final int CONVERT_ANONYMOUS_TO_NESTED_REFACTORING = 9;
//	public static final int CHANGE_METHOD_SIGNATURE_REFACTORING = 7;
//	public static final int INTRODUCE_FACTORY_REFACTORING = 11;
//	public static final int INTRODUCE_PARAMETER_REFACTORING = 12;
	public static final int MAX_RENAME_TYPES = 1;
	public static final int RENAME_TYPE = 2;
	public static final int RENAME_METHOD = 1;
	public static final int RENAME_FIELD = 0;
	public static final int RENAME_VARIABLE = 3;

}
